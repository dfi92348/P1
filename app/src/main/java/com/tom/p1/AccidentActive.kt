package com.tom.p1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import java.util.concurrent.Executor
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accident_active)
        val database: AccidentDatabase =
            Room.databaseBuilder(this, AccidentDatabase::class.java, "accident.db")
                .build()
        Executors.newSingleThreadExecutor().execute() {
            for(acc in accidentdata)
            database.accidentdao().add(acc)
        }
    }
}