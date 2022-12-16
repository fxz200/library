package com.example.myapplication

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode


class AR_test : AppCompatActivity() {
    lateinit var arFragment: ArFragment
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this)) return
        setContentView(R.layout.activity_ar_test)
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        arFragment.setOnTapArPlaneListener { hitresult: HitResult, plane: Plane, motionevent: MotionEvent? ->
            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING)
                return@setOnTapArPlaneListener
            val anchor = hitresult.createAnchor()
            placeObject(arFragment, anchor, R.raw.taro)
        }



    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun placeObject(arFragment: ArFragment, anchor: Anchor, uri: Int) {
        ModelRenderable.builder()
            .setSource(arFragment.context, uri)
            .build()
            .thenAccept { modelRenderable: ModelRenderable -> addNodeToScene(arFragment, anchor, modelRenderable) }
            .exceptionally { throwable: Throwable ->
                Toast.makeText(arFragment.getContext(), "Error:$throwable.message", Toast.LENGTH_LONG).show();
                return@exceptionally null
            }
    }
    private fun addNodeToScene(arFragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(arFragment.transformationSystem)
        node.renderable = renderable
        node.setParent(anchorNode)
        arFragment.arSceneView.scene.addChild(anchorNode)
        node.select()
    }

    private fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show()
            activity.finish()
            return false
        }
        val openGlVersionString = (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo
            .glEsVersion
        if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }
        return true
    }

    companion object {
        private const val MIN_OPENGL_VERSION = 3.0
    }

    fun finish(view: View) {}


}