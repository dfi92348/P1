package com.tom.p1

import android.app.DownloadManager
import android.app.Service
import android.content.Intent
import android.media.MediaParser
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat

import java.util.*

class MyService2 : Service() {

    companion object{
        val TAG =MyService2::class.java.simpleName
    }
    override fun onCreate() {
        super.onCreate()
        Thread{
            while (true) {
                Thread.sleep(10000)
            var soundpool = SoundPool.Builder()
                .setMaxStreams(8)
                .build()



            var C = soundpool.load(this, R.raw.sd005, 1)
                soundpool.play(C, 1.0f, 1.0f, 0, 0, 1.0f)




                val sdf = SimpleDateFormat("HH:mm:ss")
                Log.d(MyService2.TAG, "work: ${sdf.format(Date())}")

            }
        }.start()
    }




//    private fun sendRequest() {
//        val url=""
//        val req = Request.Builder()
//            .url(url)
//            .build()
//        OkHttpClient().newCall(req).enqueue(object : Callback {
//            //發送成功執行此方法
//            override fun onResponse(call: Call, response: Response) {
//                //使用 response.body?.string()取得 JSON 字串
//                Log.d("123")
//                val json = response.body?.string()
//
//
//            }
//
//        })
//
//    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder?=null
}