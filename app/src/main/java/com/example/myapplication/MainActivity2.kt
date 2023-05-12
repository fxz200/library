package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.MonitorNotifier
import android.content.Intent
import com.example.myapplication.BeaconScanPermissionsActivity
import kotlinx.android.synthetic.main.activity_main2.*
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.Identifier
import java.util.UUID


class MainActivity2 : AppCompatActivity() {
    lateinit var beaconListView: ListView
    lateinit var beaconCountTextView: TextView
    lateinit var monitoringButton: Button
    lateinit var rangingButton: Button
    lateinit var beaconReferenceApplication: BeaconReferenceApplication
    var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        beaconReferenceApplication = application as BeaconReferenceApplication

        // Set up a Live Data observer for beacon data
        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(beaconReferenceApplication.region)
        // observer will be called each time the monitored regionState changes (inside vs. outside region)
        regionViewModel.regionState.observe(this, monitoringObserver)
        // observer will be called each time a new list of beacons is ranged (typically ~1 second in the foreground)
        regionViewModel.rangedBeacons.observe(this, rangingObserver)
        regionViewModel.rangedBeacons.observe(this, distanceRange)
        regionViewModel.rangedBeacons.observe(this, distanceRange2)
        rangingButton = findViewById<Button>(R.id.rangingButton)
        monitoringButton = findViewById<Button>(R.id.monitoringButton)
        beaconListView = findViewById<ListView>(R.id.beaconList)
        beaconCountTextView = findViewById<TextView>(R.id.beaconCount)
        beaconCountTextView.text = "No beacons detected"
        beaconListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf("--"))
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }
    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()

        if (!BeaconScanPermissionsActivity.allPermissionsGranted(this,
                true)) {
            val intent = Intent(this, BeaconScanPermissionsActivity::class.java)
            intent.putExtra("backgroundAccessRequested", true)
            startActivity(intent)
        }
    }


    val monitoringObserver = Observer<Int> { state ->
        var dialogTitle = "已偵測到Beacon"
        var dialogMessage = "didEnterRegionEvent has fired"
        var stateString = "inside"
        if (state == MonitorNotifier.OUTSIDE) {
            dialogTitle = "No beacons detected"
            dialogMessage = "didExitRegionEvent has fired"
            stateString == "outside"
            beaconCountTextView.text = "Outside of the beacon region -- no beacons detected"
            beaconListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf("--"))
        }
        else {
            beaconCountTextView.text = "Inside the beacon region."
        }
        Log.d(TAG, "monitoring state changed to : $stateString")
        val builder =
            AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.setPositiveButton(android.R.string.ok, null)
        alertDialog?.dismiss()
        alertDialog = builder.create()
        alertDialog?.show()
    }

    val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "附近有 ${beacons.count()} 個beacon")
        if (BeaconManager.getInstanceForApplication(this).rangedRegions.size > 0) {
            beaconCountTextView.text = "附近有 ${beacons.count()} 個beacon"
            beaconListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                beacons
                    .sortedBy { it.distance}
                    .map { "${it.id1}\nid2: ${it.id2} id3: ${it.id3} rssi: ${it.rssi}\nest. distance: ${it.distance} m" }.toTypedArray())
        }
    }

    val uuid = "fda50693-a4e2-4fb1-afcf-c6eb07647825"
    val major = 10001
    val minor1 = 3538
    val minor2 = 2911
    val minor3 = 2912



    val distanceRange = Observer<Collection<Beacon>> { beacons ->
        if (beacons.isNotEmpty()) {
            val nearestBeacon = beacons.iterator().next()
            val distance = nearestBeacon.distance
            Log.d(TAG, "距離: ${distance} m")
            if (nearestBeacon.id1.toString() == uuid && nearestBeacon.id2.toInt() == major && nearestBeacon.id3.toInt() == minor1) {
                if (distance < 0.4) {
                    runOnUiThread {
                        beaconshow1.text = "我是3538"
                    }
                } else {
                    runOnUiThread {
                        beaconshow1.text = "大於0.4公尺"
                    }
                }
            }
            if (nearestBeacon.id1.toString() == uuid && nearestBeacon.id2.toInt() == major && nearestBeacon.id3.toInt() == minor2) {
                if (distance < 0.4) {
                    runOnUiThread {
                        beaconshow2.text = "我是2911"
                    }
                } else {
                    runOnUiThread {
                        beaconshow2.text = "大於0.4公尺"
                    }
                }
            }
        }
    }

    val distanceRange2 = Observer<Collection<Beacon>> { beacons ->
        if (beacons.isNotEmpty()) {
            val nearestBeacon = beacons.iterator().next()
            val distance = nearestBeacon.distance
            Log.d(TAG, "距離: ${distance} m")
            if (nearestBeacon.id1.toString() == uuid && nearestBeacon.id2.toInt() == major && nearestBeacon.id3.toInt() == minor3) {
                if (distance < 0.4) {
                    runOnUiThread {
                        beaconshow3.text = "我是3538"
                    }
                } else {
                    runOnUiThread {
                        beaconshow3.text = "大於0.4公尺"
                    }
                }
            }
        }
    }










    fun rangingButtonTapped(view: View) {
        val beaconManager = BeaconManager.getInstanceForApplication(this)
        if (beaconManager.rangedRegions.size == 0) {
            beaconManager.startRangingBeacons(beaconReferenceApplication.region)
            rangingButton.text = "停止偵測"
            beaconCountTextView.text = "開始偵測附近是否有beacon"
        }
        else {
            beaconManager.stopRangingBeacons(beaconReferenceApplication.region)
            rangingButton.text = "開始偵測"
            beaconCountTextView.text = "停止偵測beacon"
            beaconListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf("--"))
        }
    }

    fun monitoringButtonTapped(view: View) {
        var dialogTitle = ""
        var dialogMessage = ""
        val beaconManager = BeaconManager.getInstanceForApplication(this)
        if (beaconManager.monitoredRegions.size == 0) {
            beaconManager.startMonitoring(beaconReferenceApplication.region)
            dialogTitle = "Beacon 通知開啟"
            dialogMessage = "在偵測到beacon後會有對話框跳出"
            monitoringButton.text = "關閉通知"

        }
        else {
            beaconManager.stopMonitoring(beaconReferenceApplication.region)
            dialogTitle = "Beacon 通知關閉"
            dialogMessage = "在偵測到beacon後不會有對話框跳出"
            monitoringButton.text = "開啟通知"
        }
        val builder =
            AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.setPositiveButton(android.R.string.ok, null)
        alertDialog?.dismiss()
        alertDialog = builder.create()
        alertDialog?.show()

    }

    companion object {
        val TAG = "MainActivity2"
        val PERMISSION_REQUEST_BACKGROUND_LOCATION = 0
        val PERMISSION_REQUEST_BLUETOOTH_SCAN = 1
        val PERMISSION_REQUEST_BLUETOOTH_CONNECT = 2
        val PERMISSION_REQUEST_FINE_LOCATION = 3
    }
    fun back(view: View) {

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

}