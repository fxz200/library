package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class art : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    fun id_trans(floor:String){
        val bundle_n = Bundle()
        bundle_n.putString("datanum", floor)
        val intent = Intent(this, cam2::class.java)
        intent.putExtras(bundle_n)
        startActivity(intent)
    }
    fun back(view: View) {

        val intent = Intent(this, find::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_sl, R.anim.stop)
    }

    fun b_7_1(view: View) {
      id_trans("7B01")}

    fun b_7_2(view: View) {
        id_trans("7B02")}

    fun b_7_3(view: View) {
        id_trans("7B03")}

    fun b_7_4(view: View) {
        id_trans("7B04")}

    fun b_7_5(view: View) {
        id_trans("7B05")}

    fun b_7_6(view: View) {
        id_trans("7B06")}
}