package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class search : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    fun qrcode(view: View) {
        val intent = Intent(this,qrcode::class.java)
        startActivity(intent)
    }
}