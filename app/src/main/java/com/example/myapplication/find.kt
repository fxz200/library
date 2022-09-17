package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.philo
import com.example.myapplication.total
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_find.*

class find : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

    }
    fun back(view: View) {
        val intent = Intent(this,find::class.java)
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





