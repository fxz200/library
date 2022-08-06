package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView

class video : AppCompatActivity() {

    var VideoView:VideoView? = null

    var mediaController: MediaController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        VideoView = findViewById<View>(R.id.learningvideo) as VideoView?

        if(mediaController == null){
            mediaController = MediaController(this)
            mediaController!!.setAnchorView(this.VideoView)
        }
        VideoView!!.setMediaController(mediaController)

        VideoView!!.setVideoURI(Uri.parse("android.resource://" + packageName + "/" +R.raw.qrcode))

        VideoView!!.requestFocus()

        VideoView!!.start()

        VideoView!!.setOnCompletionListener {
            Toast.makeText(applicationContext,"教學影片結束！", Toast.LENGTH_LONG).show()
        }
    }

    fun back(view: View) {
        val intent = Intent(this,help::class.java)
        startActivity(intent)
    }

}