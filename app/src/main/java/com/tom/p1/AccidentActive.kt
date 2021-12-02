package com.tom.p1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.accident_row.view.*
import kotlinx.android.synthetic.main.activity_accident_active.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class AccidentActive : AppCompatActivity() {
    val accidentdata= arrayListOf<Accident>(
        Accident("109..","***市",1),
        Accident("109..","***市",0),
        Accident("109..","***市",2),
        Accident("109..","***市",3),
        Accident("110..","***市",1),
        Accident("108..","***市",0),
    )

    companion object {
        val TAG = AccidentActive::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accident_active)
        val database: AccidentDatabase =
            Room.databaseBuilder(this, AccidentDatabase::class.java, "accident.db")
                .build()

        Executors.newSingleThreadExecutor().execute() {

//            val data=database.accidentdao().findBylev()
//            database.accidentdao().delete(data)


            for(acc in accidentdata)
                database.accidentdao().add(acc)



        }





        CoroutineScope(Dispatchers.IO).launch {
            val expenses = database.accidentdao().getAll()
            Log.d(TAG, expenses.size.toString())
            val adapter = object : RecyclerView.Adapter<ExpenseViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                        : ExpenseViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.accident_row, parent, false)
                    return ExpenseViewHolder(view)
                }

                override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
                    val exp = expenses.get(position)
                    holder.date.setText(exp.date)
                    holder.loc.setText(exp.loc)
                    holder.lev.setText(exp.lev.toString())
                }

                override fun getItemCount(): Int {
                    return expenses.size
                }
            }
            runOnUiThread {
                recycler.setHasFixedSize(true)
                recycler.layoutManager = LinearLayoutManager(this@AccidentActive)
                recycler.adapter = adapter
            }
        }




    }



    class ExpenseViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        val date = itemView.exp_date
        val loc = itemView.exp_loc
        val lev = itemView.exp_lev
    }
}