package com.tom.p1


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccidentDAO {
    @Insert
    fun add(accident:Accident)

    @Query("select * from Accident ")
    fun getAll():List<Accident>


}