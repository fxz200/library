package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class literature : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_literature)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_sl, R.anim.stop)
    }

    fun b_6_1(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "6B01")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_6_2(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "6B02")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_6_4(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B04")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_3(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B03")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_5(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B05")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_6(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B06")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_7(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B07")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_8(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B08")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_9(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B09")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_10(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B10")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_11(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B11")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_12(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B12")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_13(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B13")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_14(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B14")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
    fun b_6_15(view: View) {        val bundle = Bundle()
        bundle.putString("datanum", "6B15" )
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)}
}