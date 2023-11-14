package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
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
import com.budiyev.android.codescanner.CodeScanner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.ar.core.*
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_ar_test.*
import kotlinx.android.synthetic.main.activity_cam2.*
import kotlinx.android.synthetic.main.activity_comic.comicinfo
import kotlinx.android.synthetic.main.activity_comic.comicinfo2
import kotlinx.android.synthetic.main.activity_main2.*



class comic : AppCompatActivity(),bottom_sheet.OnDialogButtonFragmentListener{

    lateinit var beaconReferenceApplication: BeaconReferenceApplication
    var alertDialog: android.app.AlertDialog? = null
    private lateinit var codeScanner: CodeScanner

    private lateinit var imageView: ImageView
    private var isImageVisible = true
    var hostAnchor : Anchor? = null
    ////////////////////
    var arFragment : CleanArFragment? = null
    var camera : Camera? = null
    var size = Point();
    var scene : Scene? = null
    //modles///
    //var clearmodel:ModelRenderable?=null
    var gameboy_Renderable : ModelRenderable? = null
    var beautiful_Renderable : ModelRenderable? = null
    var dog_Renderable : ModelRenderable? = null
    var funny_Renderable : ModelRenderable? = null
    var house_Renderable : ModelRenderable? = null
    var laugh_Renderable : ModelRenderable? = null
    var onepiece_Renderable : ModelRenderable? = null
    var seven_Renderable : ModelRenderable? = null
    var underwear_Renderable : ModelRenderable? = null

    //modles///
    override fun onStart() {
        super.onStart()
        // 畫面開始時檢查權限
        onClickRequestPermission()
        var a1trigger=GlobalVariable.geta1trigger()


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
        val youtubeButton = findViewById<Button>(R.id.youtube)
        val youtubeUrl = comicqrcode.yt
        if (youtubeUrl == "0") {
            youtubeButton.visibility = View.GONE
        } else {
            youtubeButton.visibility = View.VISIBLE
        }

        val bookvalue = comicqrcode.book
        qrcodebookshelf(bookvalue)
        arFragment = this.supportFragmentManager.findFragmentById(R.id.ux_fragment) as CleanArFragment
        arFragment!!.arSceneView.planeRenderer.isEnabled = false
        scene = arFragment!!.arSceneView.scene
        camera = scene!!.camera
        /////INIT////

        initbeautifulModel()
        initdogModel()
        initfunnyModel()
        inithouseModel()
        initlaughModel()
        initgameboyModel()
        initonepieceModel()
        initsevenModel()
        initunderwearModel()

        Handler(Looper.getMainLooper()).postDelayed({
            clean()
            check()
        }, 1000)
    }

    fun clean() {
        val children=scene!!.children
        for (child in children) {
            if (child is Node) {

                Toast.makeText(this, "Node name: ${child.name}",Toast.LENGTH_SHORT).show()
                Log.d("Node", "Node name: ${child.name}")
                if (child.name=="Node"){
                    println("N")
                }
                else{
                    var nodeToBeFound = scene!!.findByName(child.name)
                    scene!!.removeChild(nodeToBeFound)
                }


            }
        }
        //scene?.removeChild(stop)
    }
    fun check(){
        var model=GlobalVariable.getmodel()
        var a1trigger=GlobalVariable.geta1trigger()
        var a2trigger=GlobalVariable.geta2trigger()
        var a3trigger=GlobalVariable.geta3trigger()
        var a4trigger=GlobalVariable.geta4trigger()
        var a5trigger=GlobalVariable.geta5trigger()
        var b1trigger=GlobalVariable.getb1trigger()
        var b2trigger=GlobalVariable.getb2trigger()
        var b3trigger=GlobalVariable.getb3trigger()
        var b4trigger=GlobalVariable.getb4trigger()
        if(model=="1-1"){
            targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),dog_Renderable,"dog")
        }
        if(model=="1-2"){
            targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),gameboy_Renderable,"gameboy")
        }
        if(model=="1-3"){
            targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),underwear_Renderable,"underwear")
        }
        if(model=="1-4"){
            targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),beautiful_Renderable,"beautiful")
        }
        if(model=="1-5"){
            targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),house_Renderable,"house")
        }
        if(model=="2-1"){
            targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),onepiece_Renderable,"onepiece")
        }
        if(model=="2-2"){
            targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),funny_Renderable,"funny")
        }
        if(model=="2-3"){
            targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),laugh_Renderable,"laugh")
        }
        if(model=="2-4"){
            targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),seven_Renderable,"seven")
        }
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
                    var model=GlobalVariable.getmodel()
                    val change = findViewById<Button>(R.id.author)
                    val comic = findViewById<TextView>(R.id.comicinfo2)


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

                                GlobalVariable.setmodel("1-1")
                            }
                            check()
                        }

                        "1-2" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.seta2trigger("1")

                                GlobalVariable.setmodel("1-2")
                            }
                        }

                        "1-3" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.seta3trigger("1")

                                GlobalVariable.setmodel("1-3")
                            }
                        }
                        "1-4" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.seta4trigger("1")

                                GlobalVariable.setmodel("1-4")
                            }
                        }
                        "1-5" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.seta5trigger("1")

                                GlobalVariable.setmodel("1-5")
                            }
                        }
                        "2-1" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.setb1trigger("1")

                                GlobalVariable.setmodel("2-1")
                            }
                        }
                        "2-2" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.setb2trigger("1")

                                GlobalVariable.setmodel("2-2")
                            }
                        }
                        "2-3" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.setb3trigger("1")

                                GlobalVariable.setmodel("2-3")
                            }
                        }
                        "2-4" -> {
                            runOnUiThread {
                                comicinfo2.background=ContextCompat.getDrawable(this,R.drawable.deep_green)
                                GlobalVariable.setb4trigger("1")

                                GlobalVariable.setmodel("2-4")
                            }
                        }


                    }

                    check()
                }

            }
    }





    override fun onSelectDialog(select: String) {

        //Toast.makeText(this, "選取 $select", Toast.LENGTH_SHORT).show()
        if (select=="Share"){
            val intent = Intent(this,qr_scan::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.stop)
        }
        if (select=="Link"){

            check()

        }
    }

    ///////////INIT INIT INIT INIT////////////////////
    @TargetApi(Build.VERSION_CODES.N)
    fun initbeautifulModel() {
        ModelRenderable.builder()
            .setSource(this@comic, R.raw.beautiful)
            .build()
            .thenAccept { renderable ->
                beautiful_Renderable = renderable
            }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun initdogModel() {
        ModelRenderable.builder()
            .setSource(this@comic, R.raw.dog)
            .build()
            .thenAccept { renderable ->
                dog_Renderable = renderable

            }
    }
    @TargetApi(Build.VERSION_CODES.N)
    fun initfunnyModel() {
        ModelRenderable.builder()
            .setSource(this@comic, R.raw.funny)
            .build()
            .thenAccept { renderable ->
                funny_Renderable = renderable
            }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun inithouseModel() {
        ModelRenderable.builder()
            .setSource(this@comic, R.raw.house)
            .build()
            .thenAccept { renderable ->
                house_Renderable = renderable

            }
    }
    @TargetApi(Build.VERSION_CODES.N)
    fun initlaughModel() {
        ModelRenderable.builder()
            .setSource(this@comic, R.raw.laugh)
            .build()
            .thenAccept { renderable ->
                laugh_Renderable = renderable
            }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun initonepieceModel() {
        ModelRenderable.builder()
            .setSource(this@comic, R.raw.onepiece)
            .build()
            .thenAccept { renderable ->
                onepiece_Renderable = renderable

            }
    }
    @TargetApi(Build.VERSION_CODES.N)
    fun initsevenModel() {
        ModelRenderable.builder()
            .setSource(this@comic, R.raw.seven)
            .build()
            .thenAccept { renderable ->
                seven_Renderable = renderable
            }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun initgameboyModel() {
        ModelRenderable.builder()
            .setSource(this@comic, R.raw.test)
            .build()
            .thenAccept { renderable ->
                gameboy_Renderable = renderable

            }
    }
    @TargetApi(Build.VERSION_CODES.N)
    fun initunderwearModel() {
        ModelRenderable.builder()
            .setSource(this@comic, R.raw.underwear)
            .build()
            .thenAccept { renderable ->
                underwear_Renderable = renderable
            }
    }

    fun targetput(pos: Vector3?, scal: Vector3, model:ModelRenderable?, name:String){
        var test = TransformableNode(arFragment!!.transformationSystem)
        test.renderable = model;
        test.localPosition = pos
        test.name=name
        test.localRotation=(Quaternion.axisAngle(Vector3(1f, 0f, 0f), 90f))
        scene!!.addChild(test)
        test.rotationController.isEnabled = false
        test.scaleController.isEnabled = false
        test.translationController.isEnabled = false
        test.localScale=(scal)
    }

    fun YOUTUBE(view: View) {
        val youtubeUrl = comicqrcode.yt

        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))

        intentApp.`package` = "com.google.android.youtube"
        if (intentApp.resolveActivity(packageManager) != null) {
            startActivity(intentApp)
        } else {
            startActivity(intentBrowser)
        }
    }
}