package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }


    fun BASE(view: View) {
        
        val intent = Intent(this,usersetting::class.java)
        startActivity(intent)
    }

    fun READMOD(view: View) {

        val intent = Intent(this,read::class.java)
        startActivity(intent)
    }
    fun HELP(view: View) {

        val intent = Intent(this,help::class.java)
        startActivity(intent)
    }
    fun HIS(view: View) {

        val intent = Intent(this,history::class.java)
        startActivity(intent)
    }

    fun SEARCH(view: View) {
        val intent = Intent(this,search::class.java)
        startActivity(intent)
    }
    fun qrcode(view: View) {
        val intent = Intent(this,qrcode::class.java)
        startActivity(intent)
    }
    fun out(view: View) {
        val intent = Intent(this,login::class.java)
        startActivity(intent)
    }

}