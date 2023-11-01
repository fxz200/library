package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class natural : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_natural)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_sl, R.anim.stop)
    }

    fun b_2_5(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B05")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_6(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B06")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_7(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B07")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_8(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B08")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun b_2_9(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "2B09")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}