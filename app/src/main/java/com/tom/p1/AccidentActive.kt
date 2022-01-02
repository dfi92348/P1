package com.tom.p1

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.main.activity_accident_active.*
import java.lang.Exception
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*
import org.json.JSONObject
import java.io.*
import java.net.URL


class AccidentActive : AppCompatActivity() {
    var result = ""
    var sum = arrayOfNulls<String>(100)
    var place = arrayOfNulls<String>(100)
    var c= arrayListOf("")
    var d=arrayListOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSSLHandshake();
        setContentView(R.layout.activity_accident_active)


        val thread = Thread(mutiThread)
        thread.start()


        //將變數與 XML 元件綁定
       // val spinner = findViewById<Spinner>(R.id.spinner)
        //val gridView = findViewById<GridView>(R.id.gridView)
        val listView = findViewById<ListView>(R.id.listView)






         Thread.sleep(3000)



        listView.adapter =  ArrayAdapter(this,
            android.R.layout.simple_list_item_1,c)
    }


    private val mutiThread = Runnable {
        var con = 0
        var line: String? = ""
        val option = "taipei"
        var sumi = 0
        var cou=1;
        var placei = 0

        try {
            val puturl = URL("https://140.136.151.140/choose.php")
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
            `object`.put("option", option)
            out.writeBytes(`object`.toString())
            out.flush()
            out.close()
            os.flush()
            os.close()
            val inputStream: InputStream = putconnection.inputStream
            val bufReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))

            while (bufReader.readLine().also { line = it } != null) {

                Log.d("asd",line.toString())
                if (line != null) {
                    if (con % 2 != 0) {
                        place[placei] = line
                           c.add("$cou $line")
                        placei++
                        cou++
                    } else {
                       // sum[sumi] = line
                           d.add(line.toString())
                        sumi++
                    }
                }
                con = con + 1
                result = result.toString() + line
            }
            sumi = 0
            placei = 0
            con = 1
            result = place[2] + sum[2]
            inputStream.close()
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

