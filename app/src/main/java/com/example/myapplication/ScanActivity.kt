package com.example.myapplication

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.ar.core.Plane
import com.google.ar.sceneform.ux.ArFragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import java.io.File

class ScanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->

            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING ||
                state != AnchorState.NONE) {
                return@setOnTapArPlaneListener
            }

            val anchor = hitResult.createAnchor()
            placeObject(ux_fragment, anchor, Uri.parse("scene.sfb"))

        }
        ————————————————
        版权声明：本文为CSDN博主「fundroid_方卓」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/vitaviva/article/details/120213664

    }


}