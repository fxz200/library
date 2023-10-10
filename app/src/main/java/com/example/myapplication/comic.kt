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
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.activity_ar_test.*
import kotlinx.android.synthetic.main.activity_cam2.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import android.widget.ImageView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_comic.comicinfo2
import kotlinx.android.synthetic.main.activity_comic.comicinfo3
import kotlinx.android.synthetic.main.activity_comic.comicinfo4
import kotlinx.android.synthetic.main.activity_main2.*
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager



class comic : AppCompatActivity() {
    lateinit var beaconReferenceApplication: BeaconReferenceApplication
    var alertDialog: android.app.AlertDialog? = null

    var arFragment : CleanArFragment? = null
    var model : ModelRenderable? = null //模型对象
    var hostAnchor : Anchor? = null

    var currentStatus : AnchorStatus = AnchorStatus.EMPTY
    var statusTip : TextView? = null;   //显示当前状态的提示框
    var codeNo : EditText? = null       //显示云锚点 id 的编辑框
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
                codeNo!!.text = Editable.Factory.getInstance().newEditable(toShowStr)
            } else if (msg.what == SYNC_START) {
                statusTips.text = resources.getString(R.string.sync_progress);
            } else if (msg.what == SYNC_FAILED) {
                statusTips.text = resources.getString(R.string.sync_failed);
            } else if (msg.what == ClEAN_OVER) {
                statusTips.text = resources.getString(R.string.empty);
            }
        }
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

        initAllCompenent()
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as CleanArFragment?;
        arFragment!!.setOnTapArPlaneListener(listener)
        cleanBtn!!.setOnClickListener(clickListener)
        aynsBtn!!.setOnClickListener(clickListener)



        val textView : TextView = findViewById(R.id.comicinfo)
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("comic")

        collectionRef
            .whereEqualTo("ID", detectedValue)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val documentId = document.id
                    val pop1 = document.getString("pop1")
                    textView.setText(pop1)
                }
            }

        beaconReferenceApplication = application as BeaconReferenceApplication
        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(beaconReferenceApplication.region)
        regionViewModel.rangedBeacons.observe(this, distanceRange)
    }

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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initAllCompenent() {
        codeNo = editText
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


    fun back(view: View) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent);
    }

    val uuid = "fda50693-a4e2-4fb1-afcf-c6eb07647825"
    val major = 10001
    val minor1 = 2902
    val minor2 = 2912
    val minor3 = 3223
    val minor4 = 2903
    val minor5 = 1846

    var beacon1InRange = false
    var beacon2InRange = false
    var beacon3InRange = false
    var beacon4InRange = false
    var beacon5InRange = false

    var detectedValue: Int? = null

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
                            comicinfo.text = ""
                            comicinfo2.text = "歡迎來到漫畫區"
                            comicinfo3.text = ""
                            comicinfo4.text = ""
                            beacon1InRange = true
                            detectedValue = minor1
                        }
                    }
                }
                //2908
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor2) {
                    if (distance < 2) {
                        runOnUiThread {
                            comicinfo.text = "《犬夜叉》 冒險、愛情、妖怪"
                            comicinfo2.text = "故事圍繞著犬夜叉以及他的冒險夥伴一同尋找四魂之玉碎片，對抗強大的妖怪奈落。"
                            comicinfo3.text = "《遊戲王》 戰鬥、卡牌遊戲"
                            comicinfo4.text = "故事圍繞著主角武藤遊戲，他在玩各種競技型遊戲時，被附身的古埃及遊戲精靈法老所引導，一同參加卡片遊戲「遊戲王」的冒險。"
                            beacon2InRange = true
                            detectedValue = minor2
                        }
                    }
                }
                //3223
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor3) {
                    if (distance < 2) {
                        runOnUiThread {
                            comicinfo.text = "《美少女戰士》 少女、魔法"
                            comicinfo2.text = "故事講述青少女主角月野兔與她的朋友們，她們具有超能力和魔法，化身水手服美少女戰士對抗邪惡勢力。"
                            comicinfo3.text = "《家有賤狗》 家庭、友情、幽默"
                            comicinfo4.text = "一位單身父親丹尼‧坦納，他在妻子去世後獨自撫養三個女兒。為了照顧女兒，他請了朋友傑西、兄弟喬伊，共同居住在一個名為「Full House」的三層樓房子中。"
                            beacon3InRange = true
                            detectedValue = minor3
                        }
                    }
                }
                //2903
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor4) {
                    if (distance < 2) {
                        runOnUiThread {
                            comicinfo.text = "《笑傲江湖》 武俠、金庸"
                            comicinfo2.text = "故事背景是中國古代的江湖世界，各種武功流派和門派爭鬥不斷。故事圍繞著令狐沖展開，他在江湖中歷經艱難，結識了各種人物，涉足了各個門派，最終成為一名武林高手。"
                            comicinfo3.text = ""
                            comicinfo4.text = ""
                            beacon4InRange = true
                            detectedValue = minor4
                        }
                    }
                }
                //1846
                else if (id1.toString() == uuid && id2.toInt() == major && id3.toInt() == minor5) {
                    if (distance < 2) {
                        runOnUiThread {
                            comicinfo.text = "《航海王》 冒險、友情、正義、自由 "
                            comicinfo2.text = "描述海賊蒙其·D·魯夫 想要得到「ONE PIECE」和成為「海賊王」為夢想而出海向「偉大的航道」航行的海洋冒險故事。"
                            comicinfo3.text = "《妖怪手錶》 妖怪、友情、冒險"
                            comicinfo4.text = "故事講述主人公偶然發現一枚特殊的妖怪手錶，使他能看到妖怪世界。他與各種妖怪成為朋友。他們一起冒險，解決問題，同時揭示了妖怪的秘密。"
                            beacon5InRange = true
                            detectedValue = minor5

                        }
                    }
                }
            }
        }
    }


}