package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class philo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_philo)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_sl, R.anim.stop)
    }

    fun b_16(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B16")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_17(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B17")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_18(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B18")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_19(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B19")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_20(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B20")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_21(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B21")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_22(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B22")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_23(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B23")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_24(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B24")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_25(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B25")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_26(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B26")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_27(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B27")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }
    fun b_28(view: View) {
        val bundle = Bundle()
        bundle.putString("datanum", "1B28")
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}