package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    fun tosignup(view: View) {
        val intent = Intent(this,signup::class.java)
        startActivity(intent)
    }
    fun tologin(view: View) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

}