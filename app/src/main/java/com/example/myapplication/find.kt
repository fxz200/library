package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class find : AppCompatActivity() {
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        database = FirebaseFirestore.getInstance()

        btn_getdata.setOnClickListener
    }

    private fun getData(){
        database.collection("bookshelf").get().addOnCompleteListener(object :OnCompleteListener<QuerySnapshot>){

        }
    }
}




