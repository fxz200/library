package com.example.myapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.annotation.SuppressLint

import android.widget.TextView
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_qrcode.*
import com.google.firebase.firestore.QuerySnapshot


class total : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val db= FirebaseFirestore.getInstance()


        db.collection("bookshelf")
            .whereEqualTo("big", "total")
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val books: List<Book> = querySnapshot.toObjects(Book::class.java)
                for (i:Book in books){
                    val Textname:TextView=findViewById((R.id.f1001))
                    Textname.text = i.num
                    Textname.text = i.small
                }
            }

    }
    private lateinit var binding: ActivityMainBinding
    class Book(
        var bookID:Int=0,
        var num:String="",
        var big:String="",
        var small:String=""
    )

    @SuppressLint("SetTextI18n")



    fun back(view: View) {
        val intent = Intent(this,find::class.java)
        startActivity(intent)
    }
}