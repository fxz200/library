package com.example.myapplication
import android.os.Handler
import android.annotation.TargetApi
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.collision.Box
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main3.button2
import java.util.Random

class MainActivity3 : AppCompatActivity() {
    var arFragment : CleanArFragment? = null
    var camera : Camera? = null
    var size = Point();    //屏幕尺寸，控制子弹发射的初始位置
    var bullet : ModelRenderable? = null
    var scene : Scene? = null
    var andyRenderable : ModelRenderable? = null
    val SHOT = 0x1101       //绘制过程轨迹信号
    val SHOT_OVER = 0x1102  //清除子弹模型信号
    val SHOW_TARGET = 0x1103  //显示目标模型信号

    var handler = object : Handler() {
        override fun handleMessage(msg : Message)
        {
            if (msg.what == SHOT) { //绘制移动过程中的轨迹
                var currentStatus = msg.obj as CurrentStatus
                currentStatus.node.worldPosition = currentStatus.status

                // 轨迹与场景重叠，移除场景中重叠的点
                var nodeInContact = scene!!.overlapTest(currentStatus.node)
                if (nodeInContact != null) {
                    scene!!.removeChild(nodeInContact)
                }

            } else if (msg.what == SHOT_OVER) { //一次射击完成，清除屏幕的子弹
                var node = msg.obj as Node
                scene!!.removeChild(node)
            } else if (msg.what == SHOW_TARGET) {
                randomTarget()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        // 获取屏幕尺寸
        val display = windowManager.defaultDisplay
        display.getRealSize(size)
        arFragment = this.supportFragmentManager.findFragmentById(R.id.ux_fragment) as CleanArFragment
        arFragment!!.arSceneView.planeRenderer.isEnabled = false        //禁止 sceneform 框架的平面绘制
        scene = arFragment!!.arSceneView.scene
        camera = scene!!.camera

        initbullet()
        initTargetModel()
        button2.setOnClickListener(listener)
    }
    var listener : View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            shoot()
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    //初始化子弹模型
    private fun initbullet() {
        Texture.builder().setSource(this@MainActivity3, R.drawable.deep_green).build()
            .thenAccept { texture ->
                MaterialFactory.makeOpaqueWithTexture(this@MainActivity3, texture)
                    .thenAccept { material ->
                        // 设置子弹模型为球体
                        bullet = ShapeFactory.makeSphere(0.1f, Vector3(0f, 0f, 0f), material)
                    }
            }
    }

    private fun shoot() {
        //从屏幕发出的射线，对应子弹的运行轨迹
        var ray = camera!!.screenPointToRay(size.x / 2f, size.y / 2f);
        var node = Node()   //子弹节点
        node.renderable = bullet    //子弹节点加载子弹模型
        scene!!.addChild(node)

        Thread(object : Runnable{
            override fun run() {

                //子弹射击过程中的轨迹，子线程处理轨迹事件，主线程改变轨迹位置
                for (i in 1 .. 2000 ) {  //子弹射程 20 m
                    var stepLen = i;
                    var currentPoint = ray.getPoint(stepLen * 0.01f)
                    var msg = handler.obtainMessage()
                    msg.what = SHOT
                    msg.obj = CurrentStatus(node, currentPoint)
                    handler.sendMessage(msg)
                }

                //子弹超出距离后，从屏幕清除掉
                var msg = handler.obtainMessage()
                msg.what = SHOT_OVER
                msg.obj = node
                handler.sendMessage(msg)
            }
        }).start()
    }

    // 子线程和主线程穿点的数据类
    data class CurrentStatus(var node : Node, var status : Vector3)

    // 初始化目标模型
    @RequiresApi(Build.VERSION_CODES.N)
    fun initTargetModel() {
        ModelRenderable.builder()
            .setSource(this@MainActivity3, R.raw.taro)
            .build()
            .thenAccept { renderable ->
                andyRenderable = renderable
                handler.sendEmptyMessage(SHOW_TARGET)
            }
    }

    // 随机生成目标
    fun randomTarget() {
        for (i in 0..10) {
            val random = Random()
            val x = random.nextInt(5).toFloat()
            var z = -1 * random.nextInt(10).toFloat()
            val y = random.nextInt(2).toFloat()
            var andy = TransformableNode(arFragment!!.transformationSystem)
            andy.worldPosition = Vector3(x,y,z)
            andy.renderable = andyRenderable;
            scene!!.addChild(andy)



            andy.rotationController.isEnabled = false
            andy.scaleController.isEnabled = false
            andy.translationController.isEnabled = false

            // 限制每个模型高度 0.5 米
            val box = andy.renderable!!.getCollisionShape() as Box
            val size = box.size
            if (size != null) {
                var maxextent = if (size.x < size.y) size.y else size.x
                maxextent = if (size.z < maxextent) maxextent else size.z
                Log.e("XXX", "maxextent=$maxextent")
                if (maxextent > 0) {        //没有边界，不进行边界重绘
                    val scale = 0.5f / maxextent
                    andy.setWorldScale(Vector3(scale, scale, scale))
                }
            }
        }
    }

}