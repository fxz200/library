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


class find : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun id_trans(page:AppCompatActivity){
        val intent = Intent(this, page::class.java)
        startActivity(intent)
    }
    fun back(view: View) {

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    fun total(view: View) {
        id_trans(total())


    }
    fun philo(view: View) {
        id_trans(philo())
    }

    fun religion(view: View) {
        id_trans(religion())
    }
    fun natural(view: View) {

        id_trans(natural())
    }
    fun applied(view: View) {

        id_trans(applied())
    }
    fun social(view: View) {
        id_trans(social())
    }

    fun geography(view: View) {
        id_trans(geography())
    }
    fun chinese_geo(view: View) {

        id_trans(chinese_geo())
    }
    fun foreign_geo(view: View) {

        id_trans(foreign_geo())
    }
    fun literature(view: View) {

        id_trans(literature())
    }
    fun art(view: View) {

        id_trans(art())
    }



}