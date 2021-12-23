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
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import javax.net.ssl.TrustManager




import java.util.*
import kotlin.collections.ArrayList

class MyService2 : Service() {

    data class trans(
        var account:String,
        var data:String,
        var amount:Int,
        var type:Int,
    ){
        constructor():this("","",0,0)
        override fun toString(): String {
            return "$account $data $account $type"
        }
    }
    companion object{
        val TAG =MyService2::class.java.simpleName
    }


    override fun onCreate() {
        super.onCreate()



            Thread {

                while (GlobalVariable.gettf()==true && GlobalVariable.opensound==true) {

                    var soundpool = SoundPool.Builder()
                        .setMaxStreams(8)
                        .build()


                    var C = soundpool.load(this, R.raw.sd005, 1)
                    Thread.sleep(1000)
                    soundpool.play(C, 1.0f, 1.0f, 0, 0, 1.0f)


                    sendRequest()

                    val sdf = SimpleDateFormat("HH:mm:ss")
                    Log.d(MyService2.TAG, "work2: ${sdf.format(Date())}")
                    Thread.sleep(9000)
                }
            }.start()


    }




    private fun sendRequest() {

        val url = "https://atm201605.appspot.com/h"
        val client= OkHttpClient.Builder()
            .build()
        //建立 Request.Builder 物件，藉由 url()將網址傳入，再建立 Request 物件
        val req = Request.Builder()
            .url(url)
            .build()
        //建立 OkHttpClient 物件，藉由 newCall()發送請求，並在 enqueue()接收回傳
        CoroutineScope(Dispatchers.IO).launch {
            var respone=client.newCall(req).execute()
            respone.body?.run {
              //val json=Gson().toJson(this)
            val json=string()
            parseJSON(json)
               // Log.v("MyService2",json)
               // println(json)
            }

        }


    }


    private fun parseJSON(json:String){
        val trans= mutableListOf<trans>()
        val array=JSONArray(json)
        for(i in 0 until array.length()){
            val obj=array.optJSONObject(i)
            val account=obj.optString("account")
            val data=obj.optString("data")
            val amount=obj.optInt("amount")
            val type=obj.optInt("type")
            val t=trans(account,data,amount,type)
            Log.d(TAG,t.toString())
            trans.add(t)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }




    override fun onBind(intent: Intent): IBinder?=null
}


class MyX509TrustManager : TrustManager {

}
