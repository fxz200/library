package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.monitor
import kotlinx.android.synthetic.main.activity_usersetting.*

class usersetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usersetting)
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
    fun ABOUTUS(view: View) {
        val intent = Intent(this,aboutus::class.java)
        startActivity(intent)
    }

}