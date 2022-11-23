package com.example.myapplication

import android.app.Application

class GlobalVariable : Application() {
    companion object {
        //存放變數
        private var id: String = ""

        //修改 變數値
        fun setName(id: String){
            this.id = id
        }

        //取得 變數值
        fun getName(): String{
            return id
        }
    }
}