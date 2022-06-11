package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class favorite : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    class block(

    ){

    }
    class Book(
        var bookId:Int=0,
        var name:String="",
        var writer:String="",
        var fav:Boolean=false,
    ){

    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //val book=Book()
        val db= FirebaseFirestore.getInstance()
        //book.bookId=123456
        //book.name="呂艾電腦修好了沒"
        //book.writer="呂艾艾"
        //db.collection("Book_1").add(book)
        //---------------------------------------------------------------------//
        //var list = List<Book>()

        db.collection("Book_1")
            .whereEqualTo("fav",true)
            .get()
            .addOnSuccessListener {querySnapshot: QuerySnapshot ->
                val books: List<Book> = querySnapshot.toObjects(Book::class.java)
                for (i:Book in books){
                    val TextViewname:TextView=findViewById((R.id.booktitle))
                    TextViewname.setText("書名:"+i.name)
                    val TextViewwriter:TextView=findViewById((R.id.bookauthor))
                    TextViewwriter.setText("作者:"+i.writer)

                }
            }



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



