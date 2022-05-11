package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.Toast


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
        Toast.makeText(this, "qwe", Toast.LENGTH_SHORT).show()
    }
    fun HELP(view: View) {
        Toast.makeText(this, "qwe", Toast.LENGTH_SHORT).show()
    }
    fun HIS(view: View) {
        Toast.makeText(this, "qwe", Toast.LENGTH_SHORT).show()
    }

    fun FAV(view: View) {
        Toast.makeText(this, "qwe", Toast.LENGTH_SHORT).show()
    }

    fun SEARCH(view: View) {
        Toast.makeText(this, "qwe", Toast.LENGTH_SHORT).show()
    }
}