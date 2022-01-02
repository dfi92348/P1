package com.tom.p1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap

import android.os.Bundle
import android.provider.MediaStore


import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*
import org.json.JSONObject
import java.io.*


class CameraActivity : AppCompatActivity() {
      var selected=false
    private val ACTION_CAMERA_REQUEST_CODE = 100
    private val ACTION_ALBUM_REQUEST_CODE = 200
    var result = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSSLHandshake();
        setContentView(R.layout.activity_camera)

       val transAppButton=findViewById<Button>(R.id.transAppButton)
        cameraAppButton.setOnClickListener(cameraAppButtonHandler)
        albumAppButton.setOnClickListener(albumAppButtonHandler)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP


        transAppButton.setOnClickListener {
            val thread = Thread(mutiThread)
            thread.start()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private val mutiThread = Runnable {
        var line = ""
        val longitude =GlobalVariable.lon //傳三個變數到後端 userid longitude dimensionality
        val dimensionality = GlobalVariable.lat
        val useridd = GlobalVariable.Userid
        try {
            val puturl = URL("https://140.136.151.140/insert.php")
            val putconnection = puturl.openConnection() as HttpsURLConnection
            putconnection.setRequestProperty("Charset", "UTF-8")
            putconnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            putconnection.requestMethod = "POST"
            putconnection.doOutput = true
            putconnection.doInput = true
            putconnection.useCaches = false
            val os: OutputStream = putconnection.outputStream
            val out = DataOutputStream(os)
            val `object` = JSONObject()
            `object`.put("longitude", longitude)
            `object`.put("dimensionality", dimensionality)
            `object`.put("userid", useridd)
            out.writeBytes(`object`.toString())
            out.flush()
            out.close()
            os.flush()
            os.close()
            val inputStream = putconnection.inputStream
            val bufReader =
                BufferedReader(InputStreamReader(inputStream, "utf-8")) //後端會回傳一個complish 看你要不要
            while (bufReader.readLine().also { line = it } != null) {
                result = """
                    $result$line
                    
                    """.trimIndent()
            }
            inputStream.close()
        } catch (e: Exception) {
            result = e.toString()
        }
       // runOnUiThread { textView.setText(result) }
    }

    // 通過 intent 使用 Camera
    private fun takeImageFromCameraWithIntent() {
        println("take image from camera")
        selected=true
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, ACTION_CAMERA_REQUEST_CODE)
    }

    // 通過 intent 使用 album
    private fun takeImageFromAlbumWithIntent() {
        println("take image from album")
        selected=true
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, ACTION_ALBUM_REQUEST_CODE)
    }

    private val cameraAppButtonHandler = View.OnClickListener {

            view ->takeImageFromCameraWithIntent()

    }

    private val albumAppButtonHandler = View.OnClickListener { view ->
        takeImageFromAlbumWithIntent()
    }



    private fun displayImage(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("收到 result code $requestCode")
       if(selected){
           transAppButton.setVisibility(View.VISIBLE)
           cameraAppButton.setVisibility(View.INVISIBLE)
           albumAppButton.setVisibility(View.INVISIBLE)
       }
        when(requestCode) {

            ACTION_CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    displayImage(data.extras?.get("data") as Bitmap)
                }
            }

            ACTION_ALBUM_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val resolver = this.contentResolver
                    val bitmap = MediaStore.Images.Media.getBitmap(resolver, data?.data)
                    displayImage(bitmap)

                }
            }
            else -> {
                println("no handler onActivityReenter")
            }
        }
    }


    fun handleSSLHandshake() {
        try {
            val trustAllCerts: Array<TrustManager> =
                arrayOf<TrustManager>(object : X509TrustManager {
                    val acceptedIssuers: Array<Any?>?
                        get() = arrayOfNulls(0)

                    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        TODO("Not yet implemented")
                    }
                })
            val sc: SSLContext = SSLContext.getInstance("TLS")
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
            HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String?, session: SSLSession?): Boolean {
                    return true
                }
            })
        } catch (ignored: Exception) {
        }
    }



}