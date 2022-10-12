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
}