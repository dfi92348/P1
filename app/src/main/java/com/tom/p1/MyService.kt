package com.tom.p1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

class MyService : Service() {
    companion object{
        val TAG =MyService::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()





                Thread {
                    try {
                        Log.d(MyService.TAG, "workwork")
                        while (GlobalVariable.gettf()==true && GlobalVariable.opennoti==true) {

                            val sdf = SimpleDateFormat("HH:mm:ss")
                            Log.d(MyService.TAG, "work: ${sdf.format(Date())}")

                            noti()
                            Thread.sleep(10000)
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }.start()
               // Thread.sleep(10000)



    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder?=null

    fun noti(){
        val nm = NotificationManagerCompat.from(this) //建立通知管理物件
        //建立通知頻道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //設定頻道 Id、名稱及訊息優先權
            val name = "My Channel"

            val channel = NotificationChannel("危險", name, NotificationManager.IMPORTANCE_HIGH)
            //建立頻道
            nm.createNotificationChannel(channel)
        }
        //建立 Intent、PendingIntent，當通知被點選時開啟應用程式
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)
        //定義通知的訊息內容並發送
        val text = "危險危險"
        val builder = NotificationCompat.Builder(this, "危險")
            .setSmallIcon(android.R.drawable.btn_star_big_on) //通知圖示
            .setContentTitle("前方危險") //通知標題
            .setContentText(text) //通知內容
            .setContentIntent(pendingIntent) //通知被點選後的意圖
            .setAutoCancel(true) //通知被點選後自動消失

            .build()
        nm.notify((0..10000).random(), builder) //發送通知於裝置

    }



}