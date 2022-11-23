package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.ar.core.exceptions.FatalException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.typeOf

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    data class Info (
        var id:String="",
        var password:String="",
        var email:String=""
    ){}
    fun tosignup(view: View) {
        val intent = Intent(this,signup::class.java)
        startActivity(intent)
    }

    fun tologin(view: View) {

        val db = FirebaseFirestore.getInstance()
        val id_empty=findViewById<EditText>(R.id.userenter).text.toString().isEmpty()
        val password_empty=findViewById<EditText>(R.id.passenter).text.toString().isEmpty()
        if(id_empty==true||password_empty==true){
            Toast.makeText(this, "請填寫完整", Toast.LENGTH_SHORT).show()
        }

        else{
            val id=findViewById<EditText>(R.id.userenter).text.toString()
            val password=findViewById<EditText>(R.id.passenter).text.toString()
            try {
                db.collection("info").whereEqualTo("id","${id}")
                    .get()
                    .addOnSuccessListener {document ->
                        if (document!=null){
                            val info=document.toObjects(Info::class.java)
                            if (info.any()==false){
                                    Toast.makeText(this, "帳號不存在", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                if(password==info[0].password){
                                    val intent = Intent(this,MainActivity::class.java)
                                    startActivity(intent)
                                }
                                else{
                                    Toast.makeText(this, "密碼錯誤", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }

                    }



            }
            catch (exception:FatalException){
                Toast.makeText(this, "帳號不存在", Toast.LENGTH_SHORT).show()

            }




        }

    }

}



