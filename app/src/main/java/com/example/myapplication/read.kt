package com.example.myapplication

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission_group.CAMERA
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.SensorPrivacyManager.Sensors.CAMERA
import android.media.MediaRecorder.VideoSource.CAMERA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity


@SuppressLint("StaticFieldLeak")
class read: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        PermissionUtils.camera(this, {

        })


    }
    object PermissionUtils {
        private val RESULT_CODE_TAKE_CAMERA = 7461    //拍照
        private val RESULT_CODE_OPEN_ALBUM = 7462     //读写
        private val RESULT_CODE_SOUND_RECORD = 7463   //录音
        private val RESULT_CODE_CALL_PHONE = 10111   //拨号
        lateinit var context: Context
        private var cameraCallback: (() -> Unit)? = null        //相机回调
        private var readAndWriteCallback: (() -> Unit)? = null  //读写回调
        private var audioCallback: (() -> Unit)? = null         //录音回调
        private var callPhone: (() -> Unit)? = null         //录音回调

        /**
         * 电话权限申请
         */
        fun CallPhone(context: Context, callPhone: () -> Unit) {
            this.context = context
            this.callPhone = callPhone
            Log.e("测试电话权限申请", "ok")
            permission(context, Manifest.permission.CALL_PHONE, RESULT_CODE_CALL_PHONE, callPhone)
        }

        /**
         * 相机权限申请
         */
        fun camera(context: Context, cameraCallback: () -> Unit) {
            this.context = context
            this.cameraCallback = cameraCallback
            permission(context, Manifest.permission.CAMERA, RESULT_CODE_TAKE_CAMERA, cameraCallback)
        }


        /**
         * 读写权限申请
         */
        fun readAndWrite(context: Context, readAndWriteCallback: () -> Unit) {
            this.context = context
            this.readAndWriteCallback = readAndWriteCallback
            permissions(
                context,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA),
                RESULT_CODE_OPEN_ALBUM,
                readAndWriteCallback
            )
        }

        /**
         * 录音权限申请
         */
        fun audio(context: Context, audioCallback: () -> Unit) {
            this.context = context
            this.audioCallback = audioCallback
            permission(
                context,
                Manifest.permission.RECORD_AUDIO,
                RESULT_CODE_SOUND_RECORD,
                audioCallback
            )
        }

        /**
         * 权限申请结果
         */
        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            Log.e("测试电话权限申请3", "ok")
            when (requestCode) {

                RESULT_CODE_TAKE_CAMERA -> {    //拍照
                    if (cameraAccepted) {
                        cameraCallback?.let { it() }
                    } else {
                        //用户拒绝
                        Toast.makeText(context, "请开始拍照权限", Toast.LENGTH_SHORT).show()
                    }
                }
                RESULT_CODE_OPEN_ALBUM -> { //读写
                    if (cameraAccepted) {
                        readAndWriteCallback?.let { it() }
                    } else {
                        Toast.makeText(context, "请开启应用读取权限", Toast.LENGTH_SHORT).show()
                    }
                }
                RESULT_CODE_SOUND_RECORD -> { //录音
                    if (cameraAccepted) {
                        audioCallback?.let { it() }
                    } else {
                        Toast.makeText(context, "请开启应用录音权限", Toast.LENGTH_SHORT).show()
                    }
                }
                RESULT_CODE_CALL_PHONE -> { //拨号
                    if (cameraAccepted) {
                        callPhone?.let {
                            Log.e("已申请电话权限", "ok")
                            it()
                        }
                    } else {
                        Log.e("未申请电话权限", "ok")
                        Toast.makeText(context, "请开启应用电话权限", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //权限申请
        private fun permission(
            context: Context,
            systemCode: String,
            resultCode: Int,
            callback: () -> Unit
        ) {
            //判断是否有权限
            if (ContextCompat.checkSelfPermission(
                    context,
                    systemCode
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("已申请权限", "ok")
                callback()
            } else {
                //申请权限
                Log.e("无权限", "ok")
                ActivityCompat.requestPermissions(context as Activity, arrayOf(systemCode), resultCode)
            }
        }  //权限申请

        private fun permissions(
            context: Context,
            systemCode: Array<String>,
            resultCode: Int,
            callback: () -> Unit
        ) {
            //判断是否有权限
            if (ContextCompat.checkSelfPermission(
                    context,
                    systemCode[0]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("已申请权限", "ok")
                callback()
            } else {
                //申请权限
                Log.e("无权限", "ok")
                ActivityCompat.requestPermissions(context as Activity, systemCode, resultCode)
            }
        }
    }

    fun back(view: View) {

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    private fun isCameraAvailable(): Boolean {
        val numberOfCameras = Camera.getNumberOfCameras()
        if (numberOfCameras != 0) {
            return true
        }
        return false
    }
    private fun getCamera(): Camera? {
        var camera: Camera? = null
        try {
            camera = Camera.open()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return camera
    }


    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                // 同意
                Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                // 被拒絕過，彈出視窗告知本App需要權限的原因
                AlertDialog.Builder(this)
                    .setTitle("需要相機權限")
                    .setMessage("這個APP需要相機權限，請給予權限")
                    .setPositiveButton("Ok") { _, _ ->
                        val requestPermissionLauncher = registerForActivityResult(
                            ActivityResultContracts.RequestPermission())
                        { isGranted: Boolean ->
                            if (isGranted) {
                                Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "未取得相機權限", Toast.LENGTH_SHORT).show()
                            }
                        }
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                    .show()
            }
            else -> {
                // 第一次請求權限，直接詢問
                val requestPermissionLauncher = registerForActivityResult(
                    ActivityResultContracts.RequestPermission())
                { isGranted: Boolean ->
                    if (isGranted) {
                        Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "未取得相機權限", Toast.LENGTH_SHORT).show()
                    }
                }
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

}
