package com.tom.p1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(){

    private lateinit var mLocationProviderClient: FusedLocationProviderClient

//    private val NeedPermissions =
//        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
//    private val PERMISSION_REQUEST_CODE = 487



  companion object{
      val TA = MainActivity::class.java.simpleName
  }

    //private lateinit var notificationManager: NotificationManager
  //  @RequiresApi(Build.VERSION_CODES.O)
  //  @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toMap=findViewById<Button>(R.id.tomap)
        val toList=findViewById<Button>(R.id.tolist)
        val Remind=findViewById<Button>(R.id.remind)
        val Location=findViewById<TextView>(R.id.location)
        val Voice=findViewById<TextView>(R.id.voice)
       // setup()
        //requestLocation()
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
//

    }



//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun setup() {
//        if (!requestAllNeedPermissions())
//            return;
//     getDeviceLocation()
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    fun requestAllNeedPermissions(): Boolean {
//        val permissionsList = ArrayList<String>()
//        for (permission in NeedPermissions)
//            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
//                permissionsList.add(permission)
//        if (permissionsList.size < 1)
//            return true
//
//        ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), PERMISSION_REQUEST_CODE)
//        return false
//    }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            setup()
//        }
//    }


//    @SuppressLint("MissingPermission")
//    fun requestLocation() {
//        val mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        val locationRequest = LocationRequest.Builder()
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
////         設定更新速度
//        locationRequest.setInterval(1000)
////        設定要更新幾次座標
//        locationRequest.setNumUpdates(1)
//        mLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
//            override fun onLocationResult(locationresult: LocationResult?) {
//                locationresult ?: return
//                val locationText =
//                    "你應該是在 經度:${locationresult.lastLocation.longitude} , 緯度${locationresult.lastLocation.latitude}"
//                tv_location.setText(locationText)
//            }
//        }, null)
//    }
//


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


if(it.getString("sugar")=="開啟"){

    startService(Intent(this,MyService::class.java))
}


                val sdf = SimpleDateFormat("HH:mm:ss")
                Log.d(TA,"start: ${sdf.format(Date())}")
              if(it.getString("ice")=="開啟") {
                  startService(Intent(this, MyService2::class.java))
              }

                }
            }
        }

    fun showMaps() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }





//    private fun sendRequest() {
//
//        val url = "https://api.italkutalk.com/api/air"
//        val client= OkHttpClient.Builder()
//            .build()
//        //建立 Request.Builder 物件，藉由 url()將網址傳入，再建立 Request 物件
//        val req = Request.Builder()
//            .url(url)
//
//            .build()
//        //建立 OkHttpClient 物件，藉由 newCall()發送請求，並在 enqueue()接收回傳
//        CoroutineScope(Dispatchers.IO).launch {
//            var respone=client.newCall(req).execute()
//            respone.body?.run {
//                Log.d(TA,"okok")
//            }
//
//        }
//
//
//    }

}





