package com.example.myapplication

import android.app.Application

class GlobalVariable : Application() {
    companion object {
        //存放變數
        private var id: String = ""
        private var a1trigger:String=""
        private var a2trigger:String=""
        private var a3trigger:String=""
        private var a4trigger:String=""
        private var a5trigger:String=""
        private var b1trigger:String=""
        private var b2trigger:String=""
        private var b3trigger:String=""
        private var b4trigger:String=""
        //修改 變數値//////////////////////
        fun setName(id: String){
            this.id = id
        }
        fun seta1trigger(a1trigger: String){
            this.a1trigger = a1trigger
        }
        fun seta2trigger(a2trigger: String){
            this.a2trigger = a2trigger
        }
        fun seta3trigger(a3trigger: String){
            this.a3trigger = a3trigger
        }
        fun seta4trigger(a4trigger: String){
            this.a4trigger = a4trigger
        }
        fun seta5trigger(a5trigger: String){
            this.a5trigger = a5trigger
        }
        fun setb1trigger(b1trigger: String){
            this.b1trigger = b1trigger
        }
        fun setb2trigger(b2trigger: String){
            this.b2trigger = b2trigger
        }
        fun setb3trigger(b3trigger: String){
            this.b3trigger = b3trigger
        }
        fun setb4trigger(b4trigger: String){
            this.b4trigger = b4trigger
        }
        //取得 變數值//////////////////////////
        fun getName(): String{
            return id
        }
        fun geta1trigger(): String{
            return a1trigger
        }
        fun geta2trigger(): String{
            return a2trigger
        }
        fun geta3trigger(): String{
            return a3trigger
        }
        fun geta4trigger(): String{
            return a4trigger
        }
        fun geta5trigger(): String{
            return a5trigger
        }
        fun getb1trigger(): String{
            return b1trigger
        }
        fun getb2trigger(): String{
            return b2trigger
        }
        fun getb3trigger(): String{
            return b3trigger
        }
        fun getb4trigger(): String{
            return b4trigger
        }
    }

}