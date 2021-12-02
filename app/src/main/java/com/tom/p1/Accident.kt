package com.tom.p1
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Accident(var date:String,var loc:String,var lev:Int) {
    @PrimaryKey(autoGenerate = true)
        var id:Long=0
    }

