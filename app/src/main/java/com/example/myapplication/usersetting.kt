package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.Toast

class usersetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usersetting)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    fun MONITOR(view: View) {

        val intent = Intent(this,monitor::class.java)
        startActivity(intent)
    }
    fun FEEDBACK(view: View) {
        val intent = Intent(this,feedback::class.java)
        startActivity(intent)
    }

    fun aboutus(view: View) {
        val intent = Intent(this,applied::class.java)
        startActivity(intent)
    }


}