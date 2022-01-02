package com.tom.p1

import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback {
    //回傳權限要求後的結果
   var result=" "
    val LG = arrayOfNulls<String>(2000)
    val DL = arrayOfNulls<String>(2000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSSLHandshake();
        val thread = Thread(mutiThread)
        thread.start()
        setContentView(R.layout.activity_maps2)
        loadMap() //取得地圖元件並載入

    }



    override fun onMapReady(map: GoogleMap) {
        //檢查使用者是否已授權定位權限
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //精確定位包含粗略定位，因此只要求精確定位權限
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                0)
        } else {
            //顯示目前位置與目前位置的按鈕
            map.isMyLocationEnabled = true
            //加入標記
            val marker = MarkerOptions()



            for (i in 2..400) {
                marker.position(LatLng(DL[i]!!.toDouble(), LG[i]!!.toDouble()))
                marker.title("loction")
                marker.draggable(true)
                map.addMarker(marker)


            }
            //移動視角
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(

                    LatLng(25.035, 121.54), 13f
                )
            )
        }

    }
    private fun loadMap() {
        //連接 SupportMapFragment 元件並載入地圖
        val map = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment
        map.getMapAsync(this)
    }


    private val mutiThread = Runnable {
        var con = 0
        var line: String? = ""

        var LGi = 1
        var DLi = 0
        try {
            val puturl = URL("https://140.136.151.140/getdata.php")
            val putconnection: HttpsURLConnection = puturl.openConnection() as HttpsURLConnection
            putconnection.setRequestProperty("Charset", "UTF-8")
            putconnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            putconnection.setRequestMethod("POST")
            putconnection.setDoOutput(true)
            putconnection.setDoInput(true)
            putconnection.setUseCaches(false)
            val inputStream: InputStream = putconnection.getInputStream()
            val bufReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
            while (bufReader.readLine().also { line = it } != null) {
                if (line != null) {
                    if (con % 2 != 0) {
                        LG[LGi] = line
                        LGi++
                    } else {
                        DL[DLi] = line
                        DLi++
                    }
                }
                con = con + 1
            }
            LGi = 1
            DLi = 0
            con = 0
            inputStream.close()
            //因为我傳的是String類，所以要用例如Double.parseDouble(LG[1])来接值
            //LG是經度 DL是緯度 兩個陣列的第一個值都在陣列的第一個位置 像是LG[1] DL[1]
        } catch (e: Exception) {
            result = e.toString()
        }
       // runOnUiThread { textView.setText(result) }
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