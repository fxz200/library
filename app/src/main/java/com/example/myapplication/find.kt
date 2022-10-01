package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.Toast


class find : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    fun total(view: View) {

        val intent = Intent(this,total::class.java)
        startActivity(intent)
    }
    fun philo(view: View) {
        val intent = Intent(this,philo::class.java)
        startActivity(intent)
    }

    fun religion(view: View) {
        val intent = Intent(this,religion::class.java)
        startActivity(intent)
    }
    fun natural(view: View) {

        val intent = Intent(this,natural::class.java)
        startActivity(intent)
    }
    fun applied(view: View) {

        val intent = Intent(this,applied::class.java)
        startActivity(intent)
    }
    fun social(view: View) {
        val intent = Intent(this,social::class.java)
        startActivity(intent)
    }

    fun geography(view: View) {
        val intent = Intent(this,geography::class.java)
        startActivity(intent)
    }
    fun chinese_geo(view: View) {

        val intent = Intent(this,chinese_geo::class.java)
        startActivity(intent)
    }
    fun foreign_geo(view: View) {

        val intent = Intent(this,foreign_geo::class.java)
        startActivity(intent)
    }
    fun literature(view: View) {

        val intent = Intent(this,literature::class.java)
        startActivity(intent)
    }
    fun art(view: View) {

        val intent = Intent(this,art::class.java)
        startActivity(intent)
    }



}