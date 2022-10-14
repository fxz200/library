package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class religion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_religion)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
    }

    fun b_29(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B16")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_30(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B30")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_1(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B01")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_2(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B02")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_4(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B03")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_3(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B04")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}