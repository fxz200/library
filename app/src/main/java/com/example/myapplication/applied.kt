package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class applied : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applied)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_sl, R.anim.stop)

    }

    fun b_2_10(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B10")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_2_11(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B11")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_12(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B12")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_13(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B13")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_14(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B14")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_15(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B15")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}