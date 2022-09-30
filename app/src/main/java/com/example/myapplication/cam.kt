package com.example.myapplication
import android.R
import android.app.Activity
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class cam :  AppCompatActivity(){
    var camera: Camera? = null
    var surfaceView: SurfaceView? = null
    var surfaceHolder: SurfaceHolder? = null
    private val tag = "VideoServer"
    var start: Button? = null
    var stop: Button? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_cam)
        ////start = findViewById<View>(R.id.btn_start) as Button
        //start!!.setOnClickListener { start_camera() }
        //stop = findViewById<View>(R.id.btn_stop) as Button
        //stop!!.setOnClickListener { stop_camera() }
        //surfaceView = findViewById<View>(R.id.surface_view) as SurfaceView
        //surfaceHolder = surfaceView!!.holder
       // surfaceHolder.addCallback(this)
       // surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    private fun start_camera() {
        camera = try {
            Camera.open()
        } catch (e: RuntimeException) {
            Log.e(tag, "init_camera: $e")
            return
        }
        val param: Camera.Parameters
        param = camera?.getParameters()!!
        //modify parameter
        param.previewFrameRate = 20
        param.setPreviewSize(176, 144)
        camera?.setParameters(param)
        try {
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()
        } catch (e: Exception) {
            Log.e(tag, "init_camera: $e")
            return
        }
    }

    private fun stop_camera() {
        camera!!.stopPreview()
        camera!!.release()
    }

    //override fun surfaceChanged(arg0: SurfaceHolder, arg1: Int, arg2: Int, arg3: Int) {

    }

    //override fun surfaceCreated(holder: SurfaceHolder) {

    //}

    //override fun surfaceDestroyed(holder: SurfaceHolder) {

    //}


