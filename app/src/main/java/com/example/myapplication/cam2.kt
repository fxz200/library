package com.example.myapplication

import android.Manifest
import android.accessibilityservice.GestureDescription
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXThreads.TAG
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_cam2.*
import kotlinx.android.synthetic.main.activity_cam2.view.*
import kotlinx.android.synthetic.main.activity_monitor.view.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit
class cam2 : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
        )
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    override fun onStart() {
        super.onStart()
        // 畫面開始時檢查權限
        onClickRequestPermission()
    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            onAgree()
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // 被拒絕太多次，無法開啟請求權限視窗
                AlertDialog.Builder(this)
                    .setTitle("需要相機權限")
                    .setMessage("這個APP需要相機權限，因為被拒絕太多次，無法自動給予權限，請至設定手動開啟")
                    .setPositiveButton("Ok") { _, _ ->
                        openPermissionSettings()
                    }
                    .setNeutralButton("No") { _, _ -> onDisagree() }
                    .show()
            }
        }
    }
    private fun openPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
    //取得權限
    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
                startCamera()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                AlertDialog.Builder(this)
                    .setTitle("需要相機權限")
                    .setMessage("這個APP需要相機權限，請給予權限")
                    .setPositiveButton("Ok") { _, _ -> requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
                    .setNeutralButton("No") { _, _ -> onDisagree() }
                    .show()

            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }


    private fun onAgree() {
        Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
        startCamera()
    }

    private fun onDisagree() {
        Toast.makeText(this, "未取得相機權限", Toast.LENGTH_SHORT).show()

    }
    class Bookshelf(
        var ID:Int=0,
        var big:String="",
        var num:String="",
        var pic:String="",
        var small:String="",
    ){

    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam2)
        //////////
        val db = FirebaseFirestore.getInstance()


        val imageView : ImageView = findViewById(R.id.image)
        val bundle = intent.extras
        val datanum = bundle?.getString("datanum")
        db.collection("bookshelf").document("${datanum}")
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val bookshelf = documentSnapshot.toObject(Bookshelf::class.java)

                if (bookshelf != null) {
                    imageView.setImageResource(R.drawable.f1_002)
                    //val pic="R.drawable.${bookshelf.pic}"
                    //imageView.setImageResource(pic as Int)



                }
            }



        /////////
        if (allPermissionsGranted()) { startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }
    @SuppressLint("RestrictedApi")
    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {


            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()


            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }
    fun back(view: View) {

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}