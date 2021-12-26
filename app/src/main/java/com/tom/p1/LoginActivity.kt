package com.tom.p1

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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {

        val userid = ed_userid.text.toString()
        val passwd = ed_passwd.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val result =
                URL("https://atm201605.appspot.com/login?uid=$userid&pw=$passwd")
                    .readText()
            Log.d(TAG, result)
            if (result == "1") {
                getSharedPreferences("atm", MODE_PRIVATE)
                    .edit()
                    .putString("PREF_USERID", userid)
                    .apply()
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "登入成功", Toast.LENGTH_LONG).show()
                }
                intent.putExtra("LOGIN_USERID", userid)
                intent.putExtra("LOGIN_PASSWD", passwd)
                setResult(RESULT_OK, intent)
                finish()
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
    }


    fun cancel(view: View) {

    }
}