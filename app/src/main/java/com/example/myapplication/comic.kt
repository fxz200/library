package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.ar.core.*
import kotlinx.android.synthetic.main.activity_ar_test.*
import kotlinx.android.synthetic.main.activity_cam2.*
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
    }

    fun qrcode(view: View) {
        val intent = Intent(this,qrcode::class.java)
        startActivity(intent);
    }




    fun qrcodebookshelf(book: String) {
        val bookValue = comicqrcode.book
        when (bookValue) {
            "1-1" -> {
                runOnUiThread {
                    GlobalVariable.seta1trigger("1")
                    comicinfo.text = "《犬夜叉》"
                    comicinfo2.text = "故事圍繞著主角武藤遊戲，他在玩各種競技型遊戲時，被附身的古埃及遊戲精靈法老所引導，一同參加卡片遊戲「遊戲王」的冒險。"
                }
            }
            "1-2" -> {
                runOnUiThread {
                    GlobalVariable.seta2trigger("1")
                    comicinfo.text = "《遊戲王》"
                    comicinfo2.text = "故事圍繞著犬夜叉以及他的冒險夥伴一同尋找四魂之玉碎片，對抗強大的妖怪奈落。"
                }
            }
            "1-3" -> {
                runOnUiThread {
                    GlobalVariable.seta3trigger("1")
                    comicinfo.text = "《美少女戰士》"
                    comicinfo2.text = "故事講述青少女主角月野兔與她的朋友們，她們具有超能力和魔法，化身水手服美少女戰士對抗邪惡勢力。"
                }
            }
            "1-4" -> {
                runOnUiThread {
                    GlobalVariable.seta4trigger("1")
                    comicinfo.text = "《家有賤狗》"
                    comicinfo2.text = "一位單身父親丹尼‧坦納，他在妻子去世後獨自撫養三個女兒。為了照顧女兒，他請了朋友傑西、兄弟喬伊，共同居住在一個名為「Full House」的三層樓房子中。"
                }
            }
            "2-1" -> {
                runOnUiThread {
                    GlobalVariable.setb1trigger("1")
                    comicinfo.text = "《航海王》"
                    comicinfo2.text = "描述海賊蒙其·D·魯夫 想要得到「ONE PIECE」和成為「海賊王」為夢想而出海向「偉大的航道」航行的海洋冒險故事。"
                }
            }
            "2-2" -> {
                runOnUiThread {
                    GlobalVariable.setb2trigger("1")
                    comicinfo.text = "《妖怪手錶》"
                    comicinfo2.text = "故事講述主人公偶然發現一枚特殊的妖怪手錶，使他能看到妖怪世界。他與各種妖怪成為朋友。他們一起冒險，解決問題，同時揭示了妖怪的秘密。"
                }
            }
            "2-3" -> {
                runOnUiThread {
                    GlobalVariable.setb3trigger("1")
                    comicinfo.text = "《笑傲江湖》"
                    comicinfo2.text = "故事背景是中國古代的江湖世界，各種武功流派和門派爭鬥不斷。故事圍繞著令狐沖展開，他在江湖中歷經艱難，結識了各種人物，涉足了各個門派，最終成為一名武林高手。"
                }
            }
        }
    }





    override fun onSelectDialog(select: String) {

        Toast.makeText(this, "選取 $select", Toast.LENGTH_SHORT).show()

    }

}