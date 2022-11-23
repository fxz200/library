package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_finish.*
import kotlinx.android.synthetic.main.activity_monitor.view.*


class finish : AppCompatActivity() {
    class Review(
        var id:String="",
        var q1_1:String="",
        var q1_2:String="",
        var q1_3:String="",
        var q2_1:String="",
        var q2_2:String="",
        var q2_3:String="",
        var q2_3_text:String="",
        var q2_4:String="",
        var q2_4_text:String="",
        var score:Int=0,
        var time:Int=0

    ){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

    }
    fun data(q1:String,q2:String,q3:String,q4:String,q5:String,q6:String,q7:String,q6_text:String,q7_text:String,score:String){
        val db = FirebaseFirestore.getInstance()
        val review=Review()
        review.id="test"
        review.time=1
        review.q1_1=q1
        review.q1_2=q2
        review.q1_3=q3
        review.q2_1=q4
        review.q2_2=q5
        review.q2_3=q6
        review.q2_3_text=q6_text
        review.q2_4=q7
        review.q2_4_text=q7_text
        var score=score.substring(0,1)
        review.score=score.toInt()
        db.collection("review").add(review)
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    fun submit(view: View) {


        println("**************************************")
        var q1 = q1.findViewById<RadioButton>(q1.checkedRadioButtonId).text.toString()
        var q2=  q2.findViewById<RadioButton>(q2.checkedRadioButtonId).text.toString()
        var q3_empty= findViewById<EditText>(R.id.search_a3).text.toString().isEmpty()
        var q4=  q4.findViewById<RadioButton>(q4.checkedRadioButtonId).text.toString()
        var q5=  q5.findViewById<RadioButton>(q5.checkedRadioButtonId).text.toString()
        var q6=  q6.findViewById<RadioButton>(q6.checkedRadioButtonId).text.toString()
        var q7=  q7.findViewById<RadioButton>(q7.checkedRadioButtonId).text.toString()
        var search_a3_2hint=findViewById<EditText>(R.id.search_a3_2hint).text.toString().isEmpty()
        var search_a4_2hint=findViewById<EditText>(R.id.search_a4_2hint).text.toString().isEmpty()
        var score=score.findViewById<RadioButton>(score.checkedRadioButtonId).text.toString()
        println("**************************************")
        println(q6)
        if ((q3_empty==true)||(q6=="不太清楚，"&& search_a3_2hint==true)||(q7=="不流暢"&& search_a4_2hint==true)){
            Toast.makeText(this, "請填寫完整!", Toast.LENGTH_SHORT).show()
        }
        else{
            val q3=findViewById<EditText>(R.id.search_a3).text.toString()
            if(q6=="清楚" && q7=="流暢"){
                val q2_3=""
                val q2_4=""
                data(q1,q2,q3,q4,q5,q6,q7,q2_3,q2_4,score)
            }
            else if(q6=="清楚" && q7=="不流暢"){
                val q2_3=""
                val q2_4=findViewById<EditText>(R.id.search_a4_2hint).text.toString()
                data(q1,q2,q3,q4,q5,q6,q7,q2_3,q2_4,score)
            }
            else if(q6=="不太清楚，" && q7=="流暢"){
                val q2_3=findViewById<EditText>(R.id.search_a3_2hint).text.toString()
                val q2_4=""
                data(q1,q2,q3,q4,q5,q6,q7,q2_3,q2_4,score)
            }
            else{
                val q2_3=findViewById<EditText>(R.id.search_a3_2hint).text.toString()
                val q2_4=findViewById<EditText>(R.id.search_a4_2hint).text.toString()
                data(q1,q2,q3,q4,q5,q6,q7,q2_3,q2_4,score)
            }

        }


    }
    fun back(view: View) {

        val intent = Intent(this,cam2::class.java)
        startActivity(intent)
    }
}