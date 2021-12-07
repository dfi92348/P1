package com.tom.p1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class secactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secactivity)
        //將變數與 XML 元件綁定

        val intent= Intent("Mybroadcast")
        sendBroadcast(intent.putExtra("msg","data"))

        val btn_send = findViewById<Button>(R.id.btn_send)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val radioGroup2 = findViewById<RadioGroup>(R.id.radioGroup2)
        btn_send.setOnClickListener {


                //宣告 Bundle
                val b = Bundle()


                b.putString("sugar", radioGroup.findViewById<RadioButton>
                    (radioGroup.checkedRadioButtonId).text.toString())
                b.putString("ice", radioGroup2.findViewById<RadioButton>
                    (radioGroup2.checkedRadioButtonId).text.toString())
                //用 Activity.RESULT_OK 標記執行狀態並記錄 Intent
                setResult(Activity.RESULT_OK, Intent().putExtras(b))
                finish()

        }
    }
}