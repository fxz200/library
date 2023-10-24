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
import kotlinx.android.synthetic.main.activity_main.comicmod
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    class Companion {

    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        val bundle = intent.extras
        val id_get = bundle?.getString("id")
        var id=GlobalVariable.setName("${id_get}")

    }


    fun BASE(view: View) {
        
        val intent = Intent(this,MainActivity2::class.java)
        startActivity(intent)
    }

    fun READMOD(view: View) {

        val intent = Intent(this,read::class.java)
        startActivity(intent)
    }

    fun SEARCH(view: View) {
        val intent = Intent(this,search::class.java)
        startActivity(intent)
    }
    fun qrcode(view: View) {
        val bundle = intent.extras
        val id_get = bundle?.getString("id")
        var id=GlobalVariable.setName("${id_get}")
        val intent = Intent(this, qrcode::class.java)
        startActivity(intent)
    }
    fun out(view: View) {
        val intent = Intent(this,login::class.java)
        startActivity(intent)
    }

    fun COMIC(view: View) {
        val intent = Intent(this,qrcode::class.java)
        startActivity(intent)
    }

}