package com.tom.p1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.ContextParams
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class MyWorker(appContext:Context,workerParams: WorkerParameters):Worker(appContext,workerParams) {
    companion object{
        val TAG =MyWorker::class.java.simpleName
    }

    override fun doWork(): Result {

//        notificationManager("asdf","1123333")
//       makeNotification()
        //normal()

        Log.d(TAG, "do something")
        val sdf = SimpleDateFormat("HH:mm:ss")
        Log.d(TAG,"work: ${sdf.format(Date())}")
        return Result.success()


    }
//    private  fun makeNotification(){
//        val manager= applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        val builder=NotificationCompat.Builder()
//            .setContentTitle("this is title")
//            .setContentText("testing")
//            .setSubText("the is info")
//        manager.notify(1,builder.build())
//    }





//    private fun notificationManager(channelId:String,channalName:String):NotificationManager{
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel(
//                channelId,
//                channalName,
//                NotificationManager.IMPORTANCE_HIGH
//            ).apply {
//                description="重要"
//                enableVibration(true)
//            }.also {
//                val manager= applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//                manager.createNotificationChannel(it)
//            }
//
//
//        }
//        return manager
//    }






}