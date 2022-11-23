package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import kotlinx.android.synthetic.main.activity_qrcode.*


class qrcode : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        val r ="4"
        val e = "QRCODE錯誤，請至圖書館入口掃描。"

        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            if (it.text == r){

                val intent = Intent(this, find::class.java)

                startActivity(intent)
            }else{
                runOnUiThread{
                    textScanResult.text = e
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread{
                Toast.makeText(this, "Error:${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onStart() {
        super.onStart()
        // 畫面開始時檢查權限
        onClickRequestPermission()
    }
    private fun onAgree() {
        Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
        // 取得權限後要做的事情...
    }

    private fun onDisagree() {
        Toast.makeText(this, "未取得相機權限", Toast.LENGTH_SHORT).show()
        // 沒有取得權限的替代方案...
    }

    private val requestPermissionLauncher_QR = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        // 判斷使用者是否給予權限
        if (isGranted) {
            onAgree()
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // 被拒絕太多次，無法開啟請求權限視窗
                AlertDialog.Builder(this)
                    .setTitle("需要相機權限")
                    .setMessage("這個APP需要相機權限，因為被拒絕太多次，無法自動給予權限，請至設定手動開啟")
                    .setPositiveButton("Ok") { _, _ ->
                        // 開啟本App在設定中的權限視窗，在內心祈禱使用者願意給予權限
                        openPermissionSettings()
                    }
                    .setNeutralButton("No") { _, _ -> onDisagree() }
                    .show()
            }
        }
    }

    //取得權限
    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                // 情況一：已經同意
                Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                // 情況二：被拒絕過，彈出視窗告知本App需要權限的原因
                AlertDialog.Builder(this)
                    .setTitle("需要相機權限")
                    .setMessage("這個APP需要相機權限，請給予權限")
                    .setPositiveButton("Ok") { _, _ -> requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
                    .setNeutralButton("No") { _, _ -> onDisagree() }
                    .show()
            }
            else -> {
                // 情況三：第一次請求權限，直接請求權限
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // 開啟設定頁面
    private fun openPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "未取得相機權限", Toast.LENGTH_SHORT).show()
        }
    }



        override fun onResume(){
        super.onResume()
        codeScanner.startPreview()
    }
    override fun onPause(){
        codeScanner.releaseResources()
        super.onPause()
    }
    fun back(view: View) {

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun test(view: View) {
        val intent = Intent(this,find::class.java)
        startActivity(intent)
    }

}