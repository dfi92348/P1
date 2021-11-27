package com.tom.p1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toMap=findViewById<Button>(R.id.tomap)
        val toList=findViewById<Button>(R.id.tolist)
        val Remind=findViewById<Button>(R.id.remind)
        val Location=findViewById<TextView>(R.id.location)
        val Voice=findViewById<TextView>(R.id.voice)

        toMap.setOnClickListener {

            requestPermission()


        }

        Remind.setOnClickListener {
            val intent = Intent(this, secactivity::class.java)
            startActivityForResult(intent, 1)
        }

        toList.setOnClickListener {
            val intent = Intent(this, AccidentActive::class.java)
            startActivity(intent)
        }

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


            }
        }
    }

    fun requestPermission() {
        val result = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0)
        }else{
            showMaps()
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMaps()
            } else {
                Toast.makeText(this, "需要定位權限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showMaps() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }


}