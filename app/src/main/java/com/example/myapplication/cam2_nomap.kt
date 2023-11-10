package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.CodeScanner
import com.google.ar.core.Anchor
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.TransformableNode
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService

class cam2_nomap : AppCompatActivity() {
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
    var andyRenderable : ModelRenderable? = null
    var testRenderable : ModelRenderable? = null

    //modles///
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
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        // 畫面開始時檢查權限
        onClickRequestPermission()
        initTestModel()
        initTargetModel()
        //initModel(R.raw.test,testRenderable)


    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            onAgree()

        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {


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
        setContentView(R.layout.cam2_nomap)
     //
        val display = windowManager.defaultDisplay
        display.getRealSize(size)
        arFragment = this.supportFragmentManager.findFragmentById(R.id.ux_fragment) as CleanArFragment
        arFragment!!.arSceneView.planeRenderer.isEnabled = false        //禁止 sceneform 框架的平面绘制
        scene = arFragment!!.arSceneView.scene
        camera = scene!!.camera


        //////////
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
            val id_global= GlobalVariable.getName()
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

    @RequiresApi(Build.VERSION_CODES.N)


    ////////AR/////////


    @TargetApi(Build.VERSION_CODES.N)
    fun initTargetModel() {
        ModelRenderable.builder()
            .setSource(this@cam2_nomap, R.raw.taro)
            .build()
            .thenAccept { renderable ->
                andyRenderable = renderable
            }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun initTestModel() {
        ModelRenderable.builder()
            .setSource(this@cam2_nomap, R.raw.test)
            .build()
            .thenAccept { renderable ->
                testRenderable = renderable

            }
    }
    @RequiresApi(Build.VERSION_CODES.N)




    fun randomTarget() {

            var andy = TransformableNode(arFragment!!.transformationSystem)
            andy.renderable = andyRenderable;
            andy.worldPosition = Vector3(1f, 1f, -5f)
            andy.setWorldRotation(Quaternion.axisAngle(Vector3(0f, 1f, 0f), 180f))
            scene!!.addChild(andy)
            andy.rotationController.isEnabled = false
            andy.scaleController.isEnabled = false
            andy.translationController.isEnabled = false
            andy.setWorldScale(Vector3(10f, 10f, 10f))
    }
    fun testTarget() {
        var test = TransformableNode(arFragment!!.transformationSystem)
        test.renderable = testRenderable;
        test.worldPosition = Vector3(-1f, 1f, -5f)
        test.setWorldRotation(Quaternion.axisAngle(Vector3(1f, 0f, 0f), 90f))
        scene!!.addChild(test)
        test.rotationController.isEnabled = false
        test.scaleController.isEnabled = false
        test.translationController.isEnabled = false
        test.setWorldScale(Vector3(0.2f, 0.2f, 0.2f))
    }

    fun targetput(pos:Vector3?,scal:Vector3,model:ModelRenderable?){
        var test = TransformableNode(arFragment!!.transformationSystem)
        test.renderable = model;
        test.worldPosition = pos
        test.setWorldRotation(Quaternion.axisAngle(Vector3(1f, 0f, 0f), 90f))
        scene!!.addChild(test)
        test.rotationController.isEnabled = false
        test.scaleController.isEnabled = false
        test.translationController.isEnabled = false
        test.setWorldScale(scal)
    }



    ////////////////









    var check=0;

    fun TEST(view: View) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finish(view: View) {
        if (check>0){

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val end_time = current.format(formatter)
            record.end_time=end_time
            val id_global= GlobalVariable.getName()
            record.id=id_global



            val db = FirebaseFirestore.getInstance()

            db.collection("record").add(record)
            val intent = Intent(this, finish::class.java)
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
        val button_change : Button =findViewById(R.id.btn_capture)
        button_change.setText("抵達")

    }
     fun toggleImageVisibility() {
        if (isImageVisible) {
            imageView.visibility = View.INVISIBLE
        } else {
            imageView.visibility = View.VISIBLE
        }
        isImageVisible = !isImageVisible
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun back(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
        time();
        overridePendingTransition(R.anim.back_in, R.anim.back_out)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clean(view: View) {
        randomTarget()
        var test = TransformableNode(arFragment!!.transformationSystem)
        test.renderable = testRenderable;
        scene!!.removeChild(test)

    }

    fun ayns(view: View) {
        targetput(Vector3(-1f, 1f, -5f),Vector3(0.2f, 0.2f, 0.2f),testRenderable)

    }


}

