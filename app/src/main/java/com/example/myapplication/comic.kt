package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import bottom_sheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.ar.core.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_ar_test.*
import kotlinx.android.synthetic.main.activity_cam2.*
import kotlinx.android.synthetic.main.activity_comic.comicinfo
import kotlinx.android.synthetic.main.activity_comic.comicinfo2
import kotlinx.android.synthetic.main.activity_main2.*



class comic : AppCompatActivity(),bottom_sheet.OnDialogButtonFragmentListener{

    var alertDialog: android.app.AlertDialog? = null

    override fun onStart() {
        super.onStart()
        // 畫面開始時檢查權限
        onClickRequestPermission()

    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            onAgree()

        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {


            }
        }
    }
    private fun openPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)

    }
    fun TEST(view: View) {
        val bottomSheetFragment = bottom_sheet()
        bottomSheetFragment.listener = this@comic
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

        val bundle = Bundle()
        bundle.putString("data","1999")
        val fragment = bottom_sheet()
        fragment.arguments = bundle






    }
    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()

            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                AlertDialog.Builder(this)
                    .setTitle("需要相機權限")
                    .setMessage("這個APP需要相機權限，請給予權限")
                    .setPositiveButton("Ok") { _, _ -> requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
                    .setNeutralButton("No") { _, _ -> onDisagree() }
                    .show()


            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)

            }
        }
    }
    private fun onAgree() {
        Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()

    }

    private fun onDisagree() {
        Toast.makeText(this, "未取得相機權限", Toast.LENGTH_SHORT).show()


    }





    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this)) return
        setContentView(R.layout.activity_comic)
        val bookvalue = comicqrcode.book
        qrcodebookshelf(bookvalue)


    }

    private fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show()
            activity.finish()
            return false
        }
        val openGlVersionString = (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo
            .glEsVersion
        if (openGlVersionString.toDouble() < 3.0) {
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")


    fun back(view: View) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent);
        overridePendingTransition(R.anim.back_in, R.anim.back_out)
        comicqrcode.book = ""
    }

    //fun qrcode(view: View) {
       // val intent = Intent(this,qrcode::class.java)
       // startActivity(intent);
   // }



    class Book(
        var id:String="",
        var name:String="",
        var info:String="",
        var author:String="",
    ){

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun qrcodebookshelf(book: String) {
        var bookValue = comicqrcode.book
        if (bookValue==""){
            bookValue="default"
        }
        val db = FirebaseFirestore.getInstance()
        db.collection("comic_book").document("${bookValue}")
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val book = documentSnapshot.toObject(comic.Book::class.java)
                if (book != null) {
                    val id=book.id
                    val name=book.name
                    val info=book.info
                    val author=book.author
                    comicinfo.text = name
                    comicinfo2.text = info

                    val change = findViewById<Button>(R.id.author)
                    val comic = findViewById<TextView>(R.id.comicinfo2)
                    val book = findViewById<ImageView>(R.id.book)

                    var isTextVisible = true
                    change.setOnClickListener {
                        if (isTextVisible) {
                            comic.text = info
                            change.text = "作者介紹"
                        } else {
                            comic.text = author
                            change.text = "漫畫介紹"

                        }
                        isTextVisible = !isTextVisible
                    }

                    when (id) {
                        "1-1" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.seta1trigger("1")
                                book.setImageResource(R.drawable.dog)
                                book.alpha = 1.0f
                            }
                        }

                        "1-2" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.seta2trigger("1")
                                book.setImageResource(R.drawable.gameboy)
                                book.alpha = 1.0f
                            }
                        }

                        "1-3" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.seta3trigger("1")
                                book.setImageResource(R.drawable.beautiful)
                                book.alpha = 1.0f
                            }
                        }
                        "1-4" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.seta4trigger("1")
                                book.setImageResource(R.drawable.house)
                                book.alpha = 1.0f
                            }
                        }
                        "1-5" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.seta4trigger("1")
                                book.setImageResource(R.drawable.underwear)
                                book.alpha = 1.0f
                            }
                        }
                        "2-1" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.setb1trigger("1")
                                book.setImageResource(R.drawable.onepiece)
                                book.alpha = 1.0f
                            }
                        }
                        "2-2" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.setb2trigger("1")
                                book.setImageResource(R.drawable.funny)
                                book.alpha = 1.0f
                            }
                        }
                        "2-3" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.setb3trigger("1")
                                book.setImageResource(R.drawable.laugh)
                                book.alpha = 1.0f
                            }
                        }
                        "2-4" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.setb3trigger("1")
                                book.setImageResource(R.drawable.seven)
                                book.alpha = 1.0f
                            }
                        }


                }
            }

        }
    }





    override fun onSelectDialog(select: String) {

        Toast.makeText(this, "選取 $select", Toast.LENGTH_SHORT).show()
        if (select=="Share"){
            val intent = Intent(this,qr_scan::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.stop)
        }
    }

}