package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class geography : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geography)
    }
    fun back(view: View) {

        val intent = Intent(this,find::class.java)
        startActivity(intent)
    }
    fun b_3_6(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "3B06")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}