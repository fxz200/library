package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
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
        Toast.makeText(this, "qwe", Toast.LENGTH_SHORT).show()
    }

    fun READMOD(view: View) {

    }
    fun HELP(view: View) {

    }
    fun HIS(view: View) {

    }

    fun FAV(view: View) {

    }

    fun SEARCH(view: View) {

    }
}