package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class art : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
    }

    fun b_7_1(view: View) { val bundle = Bundle()
        bundle.putString("datanum", "7B01")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_7_2(view: View) {val bundle = Bundle()
        bundle.putString("datanum", "7B02")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_7_3(view: View) {val bundle = Bundle()
        bundle.putString("datanum", "7B03")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_7_4(view: View) {val bundle = Bundle()
        bundle.putString("datanum", "7B04")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_7_5(view: View) {val bundle = Bundle()
        bundle.putString("datanum", "7B05")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_7_6(view: View) {val bundle = Bundle()
        bundle.putString("datanum", "7B06")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
}