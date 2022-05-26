package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView




class favorite : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }



    fun heart_change(view: View) {


        val ImageView:ImageView=findViewById(R.id.heart) as ImageView



        if (ImageView.getTag().equals("select")) {

            ImageView.setTag("unSelect");
            ImageView.setImageResource(R.drawable.blank_heart)
        } else {
            ImageView.setTag("select");
            ImageView.setImageResource(R.drawable.heart)

        }
    }
}



