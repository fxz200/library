package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_qrcode.*
import com.google.firebase.firestore.QuerySnapshot


class total : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    class block(

    ){

    }
    class Book(
        var bookID:Int=0,
        var num:String="",
        var big:String="",
        var small:String=""
    )

    @SuppressLint("SetTextI18n")
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
                    val TextViewname:TextView=findViewById((R.id.f1001))
                    TextViewname.setText(i.num)
                    TextViewname.setText(i.small)
                }
            }

    }


    fun back(view: View) {
        val intent = Intent(this,find::class.java)
        startActivity(intent)
    }
}