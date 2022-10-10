package com.example.mexpense.data.trip

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tripId")
    val id: Int = 0,
    @ColumnInfo(name = "location")
    val tripLocation: String = "",
    @ColumnInfo(name = "time")
    val tripTime: String = ""
)
