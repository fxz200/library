package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class favorite : AppCompatActivity() {

    class Book(
        var bookId:Int=0,
        var name:String="",
        var writer:String="",
    ){

    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        val book=Book()
        val db= FirebaseFirestore.getInstance()
        book.bookId=123456
        book.name="呂艾電腦修好了沒"
        book.writer="呂艾艾"
        //db.collection("Book_1").add(book)
        //---------------------------------------------------------------------//
        //var list = List<books>()
        db.collection("Book_1")
            .document(
                "0TdGNbGT6HjPMMhRxrRJ")
            .get()
            .addOnSuccessListener { documentSnapshot:DocumentSnapshot->
                val books=documentSnapshot.toObject(Book::class.java)
                val TextView:TextView=findViewById((R.id.booktitle))
                if (books != null) {
                    TextView.setText("書名:"+books.name)
                }
            }

        //val book_01_name=book_01_ob.name

        /val name=book_01_ob.name

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



