package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class total : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
    }

    fun b_1(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B01")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun b_2(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B02")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_3(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B03")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_4(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B04")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_5(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B05")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_6(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B06")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_7(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B07")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_8(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B08")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_9(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B09")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_10(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B10")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_11(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B11")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_13(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B12")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_14(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B13")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_12(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B14")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_15(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B15")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}