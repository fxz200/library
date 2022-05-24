package com.example.myapplication

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission_group.CAMERA
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.SensorPrivacyManager.Sensors.CAMERA
import android.media.MediaRecorder.VideoSource.CAMERA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity


class read(sv_camera: Any) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        onClickRequestPermission()
        isCameraAvailable()
        getCamera()

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
