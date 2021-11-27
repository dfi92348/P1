package com.tom.p1

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Accident::class),version = 1)
abstract class AccidentDatabase: RoomDatabase(){
    abstract fun accidentdao():AccidentDAO
}