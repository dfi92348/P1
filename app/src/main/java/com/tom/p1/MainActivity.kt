package com.tom.p1


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.app.ActivityCompat

import android.os.Build
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {



    companion object {
        val TA = MainActivity::class.java.simpleName
        val REQUEST_CAMERA=50
        var login=false
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      if(login==false){
          login=true
          Intent(this, LoginActivity::class.java).apply {
              startActivity(this)
          }
      }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 100
            )
        }
        val history=findViewById<Button>(R.id.history)
        val Creat=findViewById<Button>(R.id.creat)
        val toMap = findViewById<Button>(R.id.tomap)
        val toList = findViewById<Button>(R.id.tolist)
        val Remind = findViewById<Button>(R.id.remind)
        val Location = findViewById<TextView>(R.id.location)
        val Voice = findViewById<TextView>(R.id.voice)
        val wel=findViewById<TextView>(R.id.textView4)


        toMap.setOnClickListener {

            showMaps()


        }

        history.setOnClickListener {
            val intent = Intent(this,com.tom.p1.history::class.java)
            startActivity(intent)

        }


        Remind.setOnClickListener {
            val intent = Intent(this, secactivity::class.java)
            startActivityForResult(intent, 1)
        }

        toList.setOnClickListener {


            val intent = Intent(this,AccidentActive::class.java)
            startActivity(intent)

        }

        Creat.setOnClickListener {
           var test=GlobalVariable.lat
            var test2=GlobalVariable.lon
            runOnUiThread {
                Toast.makeText(this@MainActivity, test.toString()+" " +test2.toString(), Toast.LENGTH_LONG).show()
            }
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        wel.text="??????\n"+GlobalVariable.Userid

    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }



        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            data?.extras?.let {
                //??????????????????????????? SecActivity ???????????????

                if (GlobalVariable.running == false){
                    startService(Intent(this, MyLocService::class.java))
            }
                if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                    //?????? Bundle ??????
                    findViewById<TextView>(R.id.voice).text =

                        "????????????:\n ${it.getString("sugar")}"


                    findViewById<TextView>(R.id.location).text =
                        "????????????:\n ${it.getString("ice")}"


                    if (it.getString("sugar") == "??????") {
                       GlobalVariable.opennoti=true
                        startService(Intent(this, MyService::class.java))
//                        val intent=Intent(this, MyLocService::class.java)
//                        intent.putExtra("2","2")
//                        startService(intent)
                        // startService(Intent(this, MyService::class.java))
                    }

                    if (it.getString("sugar") == "??????") {
                         stopService(Intent(this,MyService::class.java))
                         GlobalVariable.opennoti=false
                        Log.d("s1","stopstop")
                    }


                    if (it.getString("ice") == "??????") {
                         GlobalVariable.opensound=true
//                        val intent=Intent(this, MyLocService::class.java)
//                        intent.putExtra("1","1")
//                        startService(intent)


                         startService(Intent(this, MyService2::class.java))
                    }

                    if (it.getString("ice") == "??????") {
                        GlobalVariable.opensound=false
                        stopService(Intent(this,MyService2::class.java))
                        Log.d("s2","stopstop")


                    }

                }
            }
        }

        fun showMaps() {
            val intent = Intent(this, MapsActivity2::class.java)
            startActivity(intent)
        }

    }




//    private fun sendRequest() {
//
//        val url = "https://api.italkutalk.com/api/air"
//        val client= OkHttpClient.Builder()
//            .build()
//        //?????? Request.Builder ??????????????? url()??????????????????????????? Request ??????
//        val req = Request.Builder()
//            .url(url)
//
//            .build()
//        //?????? OkHttpClient ??????????????? newCall()????????????????????? enqueue()????????????
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







