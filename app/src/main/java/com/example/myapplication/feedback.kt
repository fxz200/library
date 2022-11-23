package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

class feedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {
        val intent = Intent(this,usersetting::class.java)
        startActivity(intent)
    }
    @SuppressLint("IntentReset")
    private fun sendEmail() {
        Log.i("Send email", "1234")

        val TO = arrayOf("fxzxz200@gmail.com")
        val CC = arrayOf("fxzxz200@gmail.com")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.putExtra(Intent.EXTRA_CC, CC)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here")

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            finish()
            Log.i("Finished sending email", "12345")
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this@feedback,
                "There is no email client installed.", Toast.LENGTH_SHORT).show()
        }

    }
    fun submit(view: View) {
        sendEmail()
        //val intent = Intent(this,MainActivity::class.java)
        //startActivity(intent)
    }

}