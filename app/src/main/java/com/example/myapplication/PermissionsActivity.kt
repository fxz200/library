package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

open class PermissionsActivity: AppCompatActivity() {

    val requestPermissionsLauncher =
        registerForActivityResult(

            ActivityResultContracts.RequestMultiplePermissions()

        ) { permissions ->

            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    println('2')
                    Log.d(TAG, "$permissionName permission granted: $isGranted")
                    //
                } else {
                    println('3')
                    Log.d(TAG, "$permissionName permission granted: $isGranted")
                    //
                }
            }
        }

    companion object {
        const val TAG = "PermissionsActivity"
    }
}

class PermissionsHelper(val context: Context) {

    fun isPermissionGranted(permissionString: String): Boolean {
        return (ContextCompat.checkSelfPermission(context, permissionString) == PackageManager.PERMISSION_GRANTED)
    }
    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun setFirstTimeAskingPermission(permissionString: String, isFirstTime: Boolean) {
        val sharedPreference = context.getSharedPreferences("com.example.myapplication",
            AppCompatActivity.MODE_PRIVATE
        )
        sharedPreference.edit().putBoolean(permissionString,
            isFirstTime).apply()
    }

    fun isFirstTimeAskingPermission(permissionString: String): Boolean {
        val sharedPreference = context.getSharedPreferences(
            "com.example.myapplication",
            AppCompatActivity.MODE_PRIVATE
        )
        return sharedPreference.getBoolean(
            permissionString,
            true
        )
    }
    fun beaconScanPermissionGroupsNeeded(backgroundAccessRequested: Boolean = false): List<Array<String>> {
        val permissions = ArrayList<Array<String>>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            permissions.add(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            if (backgroundAccessRequested) {
                permissions.add(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            permissions.add(arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT))
        }
        return permissions
    }

}



open class BeaconScanPermissionsActivity: PermissionsActivity()  {

    lateinit var layout: LinearLayout
    lateinit var permissionGroups: List<Array<String>>
    lateinit var continueButton: Button
    var scale: Float = 1.0f
        get() {
            return this.resources.displayMetrics.density
        }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println('2')
        layout = LinearLayout(this)
        layout.setPadding(dp(20))
        layout.gravity = Gravity.CENTER
        layout.setBackgroundColor(Color.BLACK)
        layout.orientation = LinearLayout.VERTICAL
        val backgroundAccessRequested = intent.getBooleanExtra("backgroundAccessRequested", true)
        val title = intent.getStringExtra("title") ?: "Permissions Needed"
        val message = intent.getStringExtra("message") ?: "In order to scan for beacons, this app requrires the following permissions from the operating system.  Please tap each button to grant each required permission."
        val continueButtonTitle = intent.getStringExtra("continueButtonTitle") ?: "繼續"
        val permissionButtonTitles = intent.getBundleExtra("permissionBundleTitles") ?: getDefaultPermissionTitlesBundle()

        permissionGroups = PermissionsHelper(this).beaconScanPermissionGroupsNeeded(backgroundAccessRequested)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(dp(0), dp(10), dp(0), dp(10))


        val titleView = TextView(this)
        titleView.gravity = Gravity.CENTER
        titleView.textSize = dp(10).toFloat()
        titleView.text = title
        titleView.layoutParams = params

        layout.addView(titleView)
        val messageView = TextView(this)
        messageView.text = message
        messageView.gravity = Gravity.CENTER
        messageView.textSize = dp(5).toFloat()
        messageView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        messageView.layoutParams = params
        layout.addView(messageView)

        var index = 0
        for (permissionGroup in permissionGroups) {
            val button = Button(this)
            val buttonTitle = permissionButtonTitles.getString(permissionGroup.first())
            button.id = index
            button.text = buttonTitle
            button.layoutParams = params
            button.setOnClickListener(buttonClickListener)
            layout.addView(button)
            index += 1
        }

        continueButton = Button(this)
        continueButton.text = continueButtonTitle
        continueButton.isEnabled = false
        continueButton.setOnClickListener {
            this.finish()
        }
        continueButton.layoutParams = params
        layout.addView(continueButton)

        setContentView(layout)
    }

    fun dp(value: Int): Int {
        return (value * scale + 0.5f).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    val buttonClickListener = View.OnClickListener { button ->
        val permissionsGroup = permissionGroups.get(button.id)
        promptForPermissions(permissionsGroup)
    }

    @SuppressLint("InlinedApi")
    fun getDefaultPermissionTitlesBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(Manifest.permission.ACCESS_FINE_LOCATION, "獲取位置")
        bundle.putString(Manifest.permission.ACCESS_BACKGROUND_LOCATION, "背景獲取位置")
        bundle.putString(Manifest.permission.BLUETOOTH_SCAN, "藍芽權限")
        return bundle
    }


    fun allPermissionGroupsGranted(): Boolean {
        for (permissionsGroup in permissionGroups) {
            if (!allPermissionsGranted(permissionsGroup)) {
                return false
            }
        }
        return true
    }

    fun setButtonColors() {
        var index = 0
        for (permissionsGroup in this.permissionGroups) {
            val button = findViewById<Button>(index)
            if (allPermissionsGranted(permissionsGroup)) {
                button.setBackgroundColor(Color.parseColor("#448844"))
            }
            else {
                button.setBackgroundColor(Color.RED)
            }
            index += 1
        }
    }
    override fun onResume() {
        super.onResume()
        setButtonColors()
        if (allPermissionGroupsGranted()) {
            continueButton.isEnabled = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun promptForPermissions(permissionsGroup: Array<String>) {
        if (!allPermissionsGranted(permissionsGroup)) {
            val firstPermission = permissionsGroup.first()

            var showRationale = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showRationale = shouldShowRequestPermissionRationale(firstPermission)
            }
            if (showRationale ||  PermissionsHelper(this).isFirstTimeAskingPermission(firstPermission)) {
                PermissionsHelper(this).setFirstTimeAskingPermission(firstPermission, false)
                requestPermissionsLauncher.launch(permissionsGroup)
            }
            else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Can't request permission")
                builder.setMessage("This permission has been previously denied to this app.  In order to grant it now, you must go to Android Settings to enable this permission.")
                builder.setPositiveButton("OK", null)
                builder.show()
            }
        }
    }
    fun allPermissionsGranted(permissionsGroup: Array<String>): Boolean {
        val permissionsHelper = PermissionsHelper(this)
        for (permission in permissionsGroup) {
            if (!permissionsHelper.isPermissionGranted(permission)) {
                return false
            }
        }
        return true
    }

    companion object {
        const val TAG = "BeaconScanPermissionActivity"
        fun allPermissionsGranted(context: Context, backgroundAccessRequested: Boolean): Boolean {
            val permissionsHelper = PermissionsHelper(context)
            val permissionsGroups = permissionsHelper.beaconScanPermissionGroupsNeeded(backgroundAccessRequested)
            for (permissionsGroup in permissionsGroups) {
                for (permission in permissionsGroup) {
                    if (!permissionsHelper.isPermissionGranted(permission)) {
                        return false
                    }
                }
            }
            return true
        }
    }
}