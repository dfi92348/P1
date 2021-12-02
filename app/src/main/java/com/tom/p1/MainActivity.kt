package com.tom.p1

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_maps.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(){

  companion object{
      val TAG = MainActivity::class.java.simpleName
  }

    private lateinit var notificationManager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toMap=findViewById<Button>(R.id.tomap)
        val toList=findViewById<Button>(R.id.tolist)
        val Remind=findViewById<Button>(R.id.remind)
        val Location=findViewById<TextView>(R.id.location)
        val Voice=findViewById<TextView>(R.id.voice)

        toMap.setOnClickListener {

            showMaps()


        }

        Remind.setOnClickListener {
            val intent = Intent(this, secactivity::class.java)
            startActivityForResult(intent, 1)
        }

        toList.setOnClickListener {

            val intent = Intent(this, AccidentActive::class.java)
            startActivity(intent)

        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(packageName, "Demo", NotificationManager.IMPORTANCE_HIGH)
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }



    }


    fun normal(view: View) {



        val notification = NotificationCompat.Builder(this, packageName)
            .setContentTitle("一般通知")
            .setContentText("收到一條消息")
            //.setSmallIcon(R.drawable.logo)
           // .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon))
            .setWhen(System.currentTimeMillis())
            //.setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify((0..10000).random(), notification)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.let {
            //驗證發出對象，確認 SecActivity 執行的狀態
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                //讀取 Bundle 資料
                findViewById<TextView>(R.id.voice).text =

                            "及時定位:\n ${it.getString("sugar")}"


                findViewById<TextView>(R.id.location).text=
                    "語音提醒:\n ${it.getString("ice")}"

                val workRequest= OneTimeWorkRequestBuilder<MyWorker>()
                    .setInitialDelay(20,TimeUnit.SECONDS)
                    .build()
                WorkManager.getInstance(this)
                    .enqueue(workRequest)

                val sdf = SimpleDateFormat("HH:mm:ss")
                Log.d(TAG,"start: ${sdf.format(Date())}")

            }
        }
    }







    fun showMaps() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }


}