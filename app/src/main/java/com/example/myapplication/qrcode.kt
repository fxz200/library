package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import kotlinx.android.synthetic.main.activity_qrcode.*
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import androidx.lifecycle.Observer
import com.example.myapplication.comicqrcode



class qrcode : AppCompatActivity() {

    lateinit var beaconReferenceApplication: BeaconReferenceApplication

    var alertDialog: android.app.AlertDialog? = null

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        beaconReferenceApplication = application as BeaconReferenceApplication
        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(beaconReferenceApplication.region)
        regionViewModel.rangedBeacons.observe(this, distanceRange)

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        val find ="4"


        val a1 = "1-1"
        val a2 = "1-2"
        val a3 = "1-3"
        val a4 = "1-4"
        val a5 = "1-5"
        val b1 = "2-1"
        val b2 = "2-2"
        val b3 = "2-3"
        val b4 = "2-4"
        val b5 = "2-5"
        val e = "QRCODE錯誤，請重新掃描。"


        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            if (it.text == find){
                val intent = Intent(this, find::class.java)
                startActivity(intent)
            } else if (it.text == a1){
                val intent = Intent(this, comic::class.java)
                startActivity(intent)
                comicqrcode.book = a1
            } else if (it.text == a2){
                val intent = Intent(this, comic::class.java)
                startActivity(intent)
                comicqrcode.book = a2
            } else if (it.text == a3){
                val intent = Intent(this, comic::class.java)
                startActivity(intent)
                comicqrcode.book = a3
            }else if (it.text == a4){
                val intent = Intent(this, comic::class.java)
                startActivity(intent)
                comicqrcode.book = a4
            } else if (it.text == b1){
                val intent = Intent(this, comic::class.java)
                startActivity(intent)
                comicqrcode.book = b1
            } else if (it.text == b2){
                val intent = Intent(this, comic::class.java)
                startActivity(intent)
                comicqrcode.book = b2
            } else if (it.text == b3){
                val intent = Intent(this, comic::class.java)
                startActivity(intent)
                comicqrcode.book = b3
            } else{
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
        overridePendingTransition(R.anim.slide_out_sl, R.anim.stop)
    }

    fun test(view: View) {
        val intent = Intent(this,find::class.java)
        startActivity(intent)
    }

    val uuid = "fda50693-a4e2-4fb1-afcf-c6eb07647825"
    val major = 10001
    val minor1 = 2902
    val minor2 = 2908   //2908
    val minor3 = 3223   //3223
    val minor4 = 2903
    val minor5 = 1846

    var beacon1InRange = false
    var beacon2InRange = false
    var beacon3InRange = false
    var beacon4InRange = false
    var beacon5InRange = false



    val distanceRange = Observer<Collection<Beacon>> { beacons ->
        if (beacons.isNotEmpty()) {
            val nearestBeacon = beacons.minByOrNull { it.distance }

            if (nearestBeacon != null) {
                val distance = nearestBeacon.distance
                val id1 = nearestBeacon.id1
                val id2 = nearestBeacon.id2
                val id3 = nearestBeacon.id3

                //2902
                if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor1) {
                    if (distance < 2) {
                        runOnUiThread {
                            textScanResult.text = "歡迎來到漫畫區"
                            beacon1InRange = true
                        }
                    }
                }
                //2908
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor2) {
                    if (distance < 2) {
                        runOnUiThread {
                            textScanResult.text = "此區域有4個QRCODE"
                            beacon2InRange = true
                        }
                    }
                }

                //3223
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor3) {
                    if (distance < 2) {
                        runOnUiThread {
                            textScanResult.text = "此區域有4個QRCODE"
                            beacon3InRange = true
                        }
                    }
                }
                //2903
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor4) {
                    if (distance < 2) {
                        runOnUiThread {
                            textScanResult.text = "此區域有3個QRCODE"
                            beacon4InRange = true
                        }
                    }
                }
                //1846
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor5) {
                    if (distance < 2) {
                        runOnUiThread {
                            textScanResult.text = "此區域有3個QRCODE"
                            beacon5InRange = true

                        }
                    }
                }
            }
        }
    }
}