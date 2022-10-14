package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class chinese_geo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chinese_geo)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
    }

    fun b_3_7(view: View) {

        val bundle = Bundle()
        bundle.putString("datanum", "3B07")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3_8(view: View) {

        val bundle = Bundle()
        bundle.putString("datanum", "3B08")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3_9(view: View) {

        val bundle = Bundle()
        bundle.putString("datanum", "3B09")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3_10(view: View) {

        val bundle = Bundle()
        bundle.putString("datanum", "3B010")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3_11(view: View) {

        val bundle = Bundle()
        bundle.putString("datanum", "3B11")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}