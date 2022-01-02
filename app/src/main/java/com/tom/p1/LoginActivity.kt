package com.tom.p1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.tom.p1.MyService.Companion.TAG
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class LoginActivity : AppCompatActivity() {
    var result = ""
    var line=" "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSSLHandshake();
        setContentView(R.layout.activity_login)

    }

    fun login(view: View) {//登陸
        val thread: Thread = Thread(mutiThread1)
        thread.start()

    }


    fun cancel(view: View) {  //註冊
        val thread: Thread = Thread(mutiThread)
        thread.start()
    }


    private val mutiThread = Runnable {
        val userid = ed_userid.text.toString()
        val password = ed_passwd.text.toString()
        try {

            val puturl = URL("https://140.136.151.140/regist.php")
            val putconnection: HttpsURLConnection = puturl.openConnection() as HttpsURLConnection
            putconnection.setRequestProperty("Charset", "UTF-8")
            putconnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            putconnection.setRequestMethod("POST")
            putconnection.setDoOutput(true)
            putconnection.setDoInput(true)
            putconnection.setUseCaches(false)
            val os: OutputStream = putconnection.getOutputStream()
            val out = DataOutputStream(os)
            val `object` = JSONObject()
            `object`.put("userid", userid)
            `object`.put("password", password)
            out.writeBytes(`object`.toString())
            out.flush()
            out.close()
            os.flush()
            os.close()
            val inputStream: InputStream = putconnection.getInputStream()
            val bufReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
            while (bufReader.readLine().also { line = it } != null) {
                result = result + line.toString() + "\n"
            }
            inputStream.close()
        } catch (e: Exception) {
            result = e.toString()
        }
        runOnUiThread {
            Toast.makeText(this@LoginActivity, "註冊成功", Toast.LENGTH_LONG).show()
        }

    }


    private val mutiThread1 = Runnable {
        val userid = ed_userid.text.toString()
        val password = ed_passwd.text.toString()
        try {
            GlobalVariable.Userid=userid
            val puturl = URL("https://140.136.151.140/login.php")
            val putconnection: HttpsURLConnection = puturl.openConnection() as HttpsURLConnection
            putconnection.setRequestProperty("Charset", "UTF-8")
            putconnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            putconnection.setRequestMethod("POST")
            putconnection.setDoOutput(true)
            putconnection.setDoInput(true)
            putconnection.setUseCaches(false)
            val os: OutputStream = putconnection.getOutputStream()
            val out = DataOutputStream(os)
            val `object` = JSONObject()
            `object`.put("userid", userid)
            `object`.put("password", password)
            out.writeBytes(`object`.toString())
            out.flush()
            out.close()
            os.flush()
            os.close()
            val inputStream: InputStream = putconnection.getInputStream()
            val bufReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
            while (bufReader.readLine().also { line = it } != null) {
                result =  line.toString()
            }
            inputStream.close()


        }
        catch (e: Exception) {
           // result = e.toString()
        }
        if (result == "true") {
            runOnUiThread {
                Toast.makeText(this@LoginActivity, "登入成功", Toast.LENGTH_LONG).show()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        } else {
            runOnUiThread {
                AlertDialog.Builder(this@LoginActivity)
                    .setTitle("車禍不單行")
                    .setMessage("登入失敗")
                    .setPositiveButton("OK", null)
                    .show()
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