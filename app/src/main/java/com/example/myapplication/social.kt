package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class social : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
    }

    fun b_2_16(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B16")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_17(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B17")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_18(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B18")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_19(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B19")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_20(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B20")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3_1(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "3B01")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3_2(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "3B02")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3_3(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "3B03")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3_4(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "3B04")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3_5(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "3B05")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}