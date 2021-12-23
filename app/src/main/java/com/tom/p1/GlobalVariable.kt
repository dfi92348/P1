package com.tom.p1

import android.app.Application

class GlobalVariable : Application() {
    companion object {
        //存放變數
         var torf: Boolean = false
         var opennoti:Boolean=false
        var opensound:Boolean=false
        //修改 變數値
        fun settf(tf : Boolean){
           torf = tf
        }
        //取得 變數值
        fun gettf(): Boolean{
            return torf
        }
    }
}