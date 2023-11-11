package com.example.myapplication

import android.widget.EditText
import android.widget.TextView

import com.google.ar.core.HitResult
import com.google.ar.core.Plane



import android.Manifest
import com.google.ar.core.Anchor
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.view.MotionEvent
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXThreads.TAG
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ComplexColorCompat.inflate
import com.example.myapplication.databinding.ActivityAboutusBinding.inflate
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.android.synthetic.main.activity_ar_test.*
import kotlinx.android.synthetic.main.activity_cam2.*
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.microedition.khronos.opengles.GL10
import android.widget.ImageView


import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main2.*
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager

import java.util.concurrent.atomic.AtomicInteger




typealias LumaListener = (luma: Double) -> Unit
class cam2 : AppCompatActivity() {
    lateinit var beaconReferenceApplication: BeaconReferenceApplication
    var alertDialog: android.app.AlertDialog? = null




    /////////////////
    @RequiresApi(Build.VERSION_CODES.O)
    private fun time() {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        println("当前日期和时间为: $formatted")
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
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
    //取得權限
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

    data class Record(
        var count:Int=0,
        var id:String="",
        var start_time:String="",
        var end_time:String="",
        var type_big:String="",
        var type_small:String="",
    ){

    }
    class Bookshelf(
        var ID:Int=0,
        var big:String="",
        var num:String="",
        var pic:String="",
        var small:String="",
    ){

    }

    val record=Record()






    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this)) return
        setContentView(R.layout.activity_cam2)
        val bundle = intent.extras
        val datanum = bundle?.getString("datanum")

        val textView : TextView = findViewById(R.id.floor)
        val db = FirebaseFirestore.getInstance()

        db.collection("bookshelf").document("${datanum}")
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val bookshelf = documentSnapshot.toObject(Bookshelf::class.java)
                if (bookshelf != null) {
                    val id=bookshelf.ID
                    val id_str = "${id}"
                    val floor=id_str.substring(0,1)
                    val floor_text="  請搭乘電梯至 ${floor} 樓  "
                    textView.setText(floor_text)
                    record.type_big=bookshelf.big
                    record.type_small=bookshelf.small

                }
            }
        val test=true
        if(test==true){

            val db = FirebaseFirestore.getInstance()
            val id_global=GlobalVariable.getName()
            ///////////////////////////////////////////////

            println(id_global)
            db.collection("record").whereEqualTo("id","${id_global}")
                .get()
                .addOnSuccessListener {document->
                    if(document!=null){
                        val counts=document.toObjects(Record::class.java)

                        println("*************************************")
                        println(counts)
                        if(counts.any()==false){
                            record.count=1
                        }
                        else{
                            println("*************************************")
                            println(counts.size)
                            val num_count=counts.size+1
                            record.count=num_count
                        }
                    }
                    else{
                        Toast.makeText(this, "missing", Toast.LENGTH_SHORT).show()
                    }
                }



            ///////////////////////////////////////////////
        }

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

        val start_time= current.format(formatter)
        record.start_time=start_time



    }



    ////////AR/////////





    ////////////////
    @RequiresApi(Build.VERSION_CODES.N)
    private fun placeObject(arFragment: ArFragment, anchor: Anchor, uri: Int) {
        ModelRenderable.builder()
            .setSource(arFragment.context, uri)
            .build()
            .thenAccept { modelRenderable: ModelRenderable -> addNodeToScene(arFragment, anchor, modelRenderable) }
            .exceptionally { throwable: Throwable ->
                Toast.makeText(arFragment.getContext(), "Error:$throwable.message", Toast.LENGTH_LONG).show();
                return@exceptionally null
            }
    }
    private fun addNodeToScene(arFragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(arFragment.transformationSystem)
        node.renderable = renderable
        node.setParent(anchorNode)
        arFragment.arSceneView.scene.addChild(anchorNode)
        node.select()
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


    var check=0;



    @RequiresApi(Build.VERSION_CODES.O)
    fun finish(view: View) {
        if (check>0){

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val end_time = current.format(formatter)
            record.end_time=end_time
            val id_global=GlobalVariable.getName()
            record.id=id_global



            val db = FirebaseFirestore.getInstance()

            db.collection("record").add(record)
            val intent = Intent(this,finish::class.java)
            startActivity(intent)
        }

        //val db = FirebaseFirestore.getInstance()
        val db = FirebaseFirestore.getInstance()
        val textView : TextView = findViewById(R.id.floor)
        val imageView : ImageView = findViewById(R.id.librarymap)
        val bundle = intent.extras
        val datanum = bundle?.getString("datanum")
        textView.setText("")
        db.collection("bookshelf").document("${datanum}")
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val bookshelf = documentSnapshot.toObject(Bookshelf::class.java)
                if (bookshelf != null) {
                    val pic=bookshelf.pic;
                    val mDrawableName = "${pic}"
                    val resID = resources.getIdentifier(mDrawableName, "drawable", packageName)
                    imageView.setImageResource(resID);
                    //record.type_big=bookshelf.big
                    //record.type_small=bookshelf.small


                }
            }
        check=1;
        val button_change :Button=findViewById(R.id.btn_capture)
        button_change.setText("抵達")

    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun back(view: View) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent);
        time();
        overridePendingTransition(R.anim.back_in, R.anim.back_out)

    }
    var cons="1"
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun Map(view: View) {

        if (cons=="1"){
            val map = findViewById<ImageView>(R.id.librarymap)
            val btn= findViewById<Button>(R.id.test)
            map.alpha=(0.0f)
            btn.text="開啟地圖"
            cons="0"
        }
       else{
            val map = findViewById<ImageView>(R.id.librarymap)
            val btn= findViewById<Button>(R.id.test)
            map.alpha=(1.0f)

            btn.text="關閉地圖"
            cons="1"
       }

    }


}