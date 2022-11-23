package com.example.myapplication

import android.content.Intent
import android.content.LocusId
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }
    class Info (
        var id:String="",
        var password:String="",
        var email:String=""
    ){}
    fun tosignup(view: View) {



    }

    fun login(view: View) {

        val db = FirebaseFirestore.getInstance()
        val info=Info()
        val id_empty=findViewById<EditText>(R.id.userenter).text.toString().isEmpty()
        val password_empty=findViewById<EditText>(R.id.passenter).text.toString().isEmpty()
        val email_empty=findViewById<EditText>(R.id.emailenter).text.toString().isEmpty()
        if(id_empty==true||password_empty==true||email_empty==true){
            Toast.makeText(this, "請填寫完整", Toast.LENGTH_SHORT).show()
        }
        //學號識別
        else{
            val id=findViewById<EditText>(R.id.userenter).text.toString()
            val password=findViewById<EditText>(R.id.passenter).text.toString()
            val email=findViewById<EditText>(R.id.emailenter).text.toString()
            info.id=id
            info.password=password
            info.email=email
            db.collection("info").add(info)
            val intent = Intent(this,login::class.java)
            startActivity(intent)
        }
    }


}