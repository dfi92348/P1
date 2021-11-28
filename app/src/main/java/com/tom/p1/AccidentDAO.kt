package com.tom.p1


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccidentDAO {
    @Insert
    fun add(accident:Accident)

    @Delete
    fun delete(accident: List<Accident>)

    @Query("SELECT * FROM Accident WHERE lev=1")
    fun findBylev():List<Accident>

    @Query("select * from Accident ")
    fun getAll():List<Accident>


}