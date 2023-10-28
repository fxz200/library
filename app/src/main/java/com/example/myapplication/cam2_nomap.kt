package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
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
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.impl.Observable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import bottom_sheet
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cam2.ayns
import kotlinx.android.synthetic.main.activity_cam2.clean
import kotlinx.android.synthetic.main.activity_cam2.comicinfo
import kotlinx.android.synthetic.main.activity_cam2.editText
import kotlinx.android.synthetic.main.activity_cam2.statusTips
import kotlinx.android.synthetic.main.bottom_sheet.bottom_sheet
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.w3c.dom.Text
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService

class cam2_nomap : AppCompatActivity(),bottom_sheet.OnDialogButtonFragmentListener {
    lateinit var beaconReferenceApplication: BeaconReferenceApplication
    var alertDialog: android.app.AlertDialog? = null
    private lateinit var codeScanner: CodeScanner

    var arFragment : CleanArFragment? = null
    var model : ModelRenderable? = null //模型对象
    var hostAnchor : Anchor? = null     //被绘制的锚点信息（代表本地设置的锚点或者云锚点）
    /**
     * 锚点状态机，只允许设置一个锚点，有多余锚点不允许添加
     */
    var currentStatus : AnchorStatus = AnchorStatus.EMPTY
    var statusTip : TextView? = null;   //显示当前状态的提示框
    var codeNo : TextView? = null       //显示云锚点 id 的编辑框
    var cleanBtn : Button? = null       //清理锚点按钮
    var aynsBtn : Button? = null        //获取云锚点按钮
    var listNode : MutableList<Node> = ArrayList()      //记录被渲染的锚点
    val ClEAN_OVER = 0x1100             //清理界面锚点信号
    val SYNC_START = 0x1101             //开始同步信号
    val SYNC_OVER = 0x1102              //同步完成信号
    val SYNC_FAILED = 0x1103            //同步失败信号
    var handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg : Message) {
            if (msg.what == SYNC_OVER) {
                statusTips.text = resources.getString(R.string.sync_over);
                var toShowStr = msg.obj as String

            } else if (msg.what == SYNC_START) {
                statusTips.text = resources.getString(R.string.sync_progress);
            } else if (msg.what == SYNC_FAILED) {
                statusTips.text = resources.getString(R.string.sync_failed);
            } else if (msg.what == ClEAN_OVER) {
                statusTips.text = resources.getString(R.string.empty);
            }
        }
    }
    private lateinit var imageView: ImageView
    private var isImageVisible = true


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
        if (!checkIsSupportedDeviceOrFinish(this)) return
        setContentView(R.layout.cam2_nomap)



        //imageView = findViewById(R.id.librarymap)
        //imageView.setOnClickListener{
            //toggleImageVisibility()
        //}
        ///QR///




        ////////AR////

        initAllCompenent()
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as CleanArFragment?;
        arFragment!!.setOnTapArPlaneListener(listener)
        cleanBtn!!.setOnClickListener(clickListener)
        aynsBtn!!.setOnClickListener(clickListener)
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

        //////////Beacon/////////
        beaconReferenceApplication = application as BeaconReferenceApplication
        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(beaconReferenceApplication.region)
        regionViewModel.rangedBeacons.observe(this, distanceRange)
        //regionViewModel.rangedBeacons.observe(this, distanceRange2)


    }



    ////////AR/////////



    private var clickListener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            when(p0!!.id)
            {
                cleanBtn!!.id -> {
                    println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH")
                    //因为存在线程处理状态机的状态，加了并发控制锁
                    synchronized(currentStatus)
                    {
                        cleanAllNode()
                        currentStatus = AnchorStatus.EMPTY      //每次清理锚点，置状态机为初始状态
                        handler.sendEmptyMessage(ClEAN_OVER)    //通知界面改变提示信息
                    }
                }
                aynsBtn!!.id -> {
                    if (codeNo!!.text.length <= 0) {    //如果没有云锚点的索引 ID 不使用云锚点
                        return
                    }
                    var str = codeNo!!.text.toString()
                    arFragment!!.arSceneView.planeRenderer.isEnabled = false
                    hostAnchor = arFragment!!.arSceneView.session!!.resolveCloudAnchor(str)
                    placeModel()
                }
            }
        }
    }


    //清除界面锚点包括云锚点和本地锚点
    private fun cleanAllNode() {
        //没有节点被渲染，就不清空锚点集合
        if (listNode.size == 0) {
            return
        }

        //从界面清除被渲染的锚点
        for (i in 0 .. listNode.lastIndex) {
            arFragment!!.getArSceneView().getScene().removeChild(listNode.get(i))
        }
        //清空记录的渲染锚点集合
        listNode.clear()
    }

    //界面空间映射，初始化模型资源
    @RequiresApi(Build.VERSION_CODES.N)
    private fun initAllCompenent() {
        val textView : TextView = findViewById(R.id.editText)
        codeNo = textView
        cleanBtn = clean
        aynsBtn = ayns
        statusTip = statusTips

        ModelRenderable.builder().setSource(this, R.raw.taro)
            .build().thenAccept { renderable -> model = renderable }
            .exceptionally ({ it -> Log.e("XXX", "xxx"); null })
    }



    //设置放置模型的点击事件的监听器
    var listener = object : BaseArFragment.OnTapArPlaneListener {
        override fun onTapPlane(hitResult: HitResult?, plane: Plane?, motionEvent: MotionEvent?) {

            //模型资源加载失败，不对锚点进行渲染处理
            if (model == null)
                return
            synchronized(currentStatus)
            {
                //不是初始状态，不对锚点渲染模型，用于限制只有一个锚点模型
                //不是初始状态即表明有一个锚点已经渲染
                if (currentStatus != AnchorStatus.EMPTY) {
                    return
                }
            }

            //设置绘制的锚点为当前的本地锚点，同时将本地锚点同步至 google 的服务
            hostAnchor = arFragment!!.arSceneView.session!!.hostCloudAnchor(hitResult!!.createAnchor())
            run2Test()
            placeModel()
        }
    }

    //在锚点上渲染模型
    private fun placeModel() {
        var node = AnchorNode(hostAnchor)
        arFragment!!.getArSceneView().getScene().addChild(node)
        var andy = TransformableNode(arFragment!!.transformationSystem)
        andy.setParent(node)
        andy.renderable = model
        andy.select()
        listNode.add(node) //每次在界面上对锚点渲染（加载）3D 模型，就将当前被操作的锚点记录下来
    }

    //开线程获取本地锚点的同步状态（同时刷新状态机）
    private fun run2Test() {
        Thread(object : Runnable {
            override fun run() {
                //只要状态机线程跑起来，就设置状态机为同步中的状态
                synchronized(currentStatus)
                {
                    currentStatus = AnchorStatus.HOSTING
                }
                //通知界面刷新同步中的状态提示
                handler.sendEmptyMessage(SYNC_START)

                //死循环检测锚点同步状态（暂时未发现回调）
                loop@while (true) {
                    Log.e("XXX", "keep running")
                    Thread.sleep(1000)
                    var tag = SYNC_START
                    var showTip = ""
                    synchronized(currentStatus)
                    {

                        if (hostAnchor!!.cloudAnchorState == Anchor.CloudAnchorState.SUCCESS) {
                            //同步完成，并且成功
                            currentStatus = AnchorStatus.HOSTED     //调整状态机为同步完成
                            Log.e("XXX", "run2Test 1 currentStatus = " + currentStatus)
                            tag = SYNC_OVER
                            showTip = hostAnchor!!.cloudAnchorId
                        } else if (hostAnchor!!.cloudAnchorState == Anchor.CloudAnchorState.TASK_IN_PROGRESS) {
                            //同步中的状态不做任何处理，也不跳出死循环
                        } else {
                            //同步完成，但是失败
                            currentStatus = AnchorStatus.HOST_FAILED  //调整状态机为同步失败
                            Log.e("XXX", "run2Test 2 currentStatus = " + currentStatus)
                            showTip = "" + hostAnchor!!.cloudAnchorState
                            tag = SYNC_FAILED
                        }
                    }
                    when (tag){
                        SYNC_OVER, SYNC_FAILED -> {
                            var msg = handler.obtainMessage()
                            msg.what = tag
                            msg.obj = showTip
                            handler.sendMessage(msg)
                            break@loop
                        }
                    }
                }
            }
        }).start()
    }


    ////////////////
    @RequiresApi(Build.VERSION_CODES.N)
    private fun placeObject(arFragment: ArFragment, anchor: Anchor, uri: Int) {
        ModelRenderable.builder()
            .setSource(arFragment.context, uri)
            .build()
            .thenAccept { modelRenderable: ModelRenderable -> addNodeToScene(arFragment, anchor, modelRenderable) }
            .exceptionally { throwable: Throwable ->
                Toast.makeText(
                    arFragment.getContext(),
                    "Error:$throwable.message",
                    Toast.LENGTH_LONG
                ).show();
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
        val openGlVersionString = (activity.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
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

    fun TEST(view: View) {
        val bottomSheetFragment = bottom_sheet()
        bottomSheetFragment.listener = this@cam2_nomap
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
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

    }

    val uuid = "fda50693-a4e2-4fb1-afcf-c6eb07647825"
    val major = 10001
    val minor1 = 2912
    val minor2 = 2908
    val minor3 = 3223
    val minor4 = 2903
    val minor5 = 1846

    var beacon1InRange = false
    var beacon2InRange = false
    var beacon3InRange = false
    var beacon4InRange = false
    var beacon5InRange = false


    val distanceRange = Observer<Collection<Beacon>> { beacons ->
        if (beacons.isNotEmpty()) {
            val nearestBeacon = beacons.minByOrNull { it.distance }

            if (nearestBeacon != null) {
                val distance = nearestBeacon.distance
                val id1 = nearestBeacon.id1
                val id2 = nearestBeacon.id2
                val id3 = nearestBeacon.id3

                //2902
                if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor1) {
                    if (distance < 2) {
                        runOnUiThread {
                            beacon1InRange = true
                            comicinfo.text = "歡迎來到漫畫區"
                            var str = "ua-be730ffd5325c030f76875dc7f10374f"
                            arFragment!!.arSceneView.planeRenderer.isEnabled = false
                            hostAnchor = arFragment!!.arSceneView.session!!.resolveCloudAnchor(str)
                            placeModel()
                        }
                    }
                }
                //2908
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor2) {
                    if (distance < 2) {
                        runOnUiThread {
                            beacon2InRange = true
                            comicinfo.text = "2"
                            var str = "ua-a5cbfdad1b84376e0fcca1567198170b"
                            arFragment!!.arSceneView.planeRenderer.isEnabled = false
                            hostAnchor = arFragment!!.arSceneView.session!!.resolveCloudAnchor(str)
                            placeModel()
                        }
                    }
                }
                //3223
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor3) {
                    if (distance < 2) {
                        runOnUiThread {
                            beacon3InRange = true
                            comicinfo.text = "2"
                            var str = "ua-a5cbfdad1b84376e0fcca1567198170b"
                            arFragment!!.arSceneView.planeRenderer.isEnabled = false
                            hostAnchor = arFragment!!.arSceneView.session!!.resolveCloudAnchor(str)
                            placeModel()
                        }
                    }
                }
                //290
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor4) {
                    if (distance < 2) {
                        runOnUiThread {
                            beacon4InRange = true
                            comicinfo.text = "2"
                            var str = "ua-a5cbfdad1b84376e0fcca1567198170b"
                            arFragment!!.arSceneView.planeRenderer.isEnabled = false
                            hostAnchor = arFragment!!.arSceneView.session!!.resolveCloudAnchor(str)
                            placeModel()
                        }
                    }
                }
                //1846
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor5) {
                    if (distance < 2) {
                        runOnUiThread {
                            beacon5InRange = true
                            comicinfo.text = "2"
                            var str = "ua-a5cbfdad1b84376e0fcca1567198170b"
                            arFragment!!.arSceneView.planeRenderer.isEnabled = false
                            hostAnchor = arFragment!!.arSceneView.session!!.resolveCloudAnchor(str)
                            placeModel()
                        }
                    }
                }
            }
        }
    }
    override fun onSelectDialog(select: String) {
        Toast.makeText(this, "選取 $select", Toast.LENGTH_SHORT).show()
        lateinit var codeScanner: CodeScanner
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)
        codeScanner.startPreview()
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread{
                Toast.makeText(this, "Error:${it.message}", Toast.LENGTH_LONG).show()
            }
        }



    }

}