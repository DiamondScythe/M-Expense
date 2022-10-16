package com.example.mexpense.data.trip

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val tripId: Int = 0,
    @ColumnInfo(name = "location")
    val tripLocation: String = "",
    @ColumnInfo(name = "time")
    val tripTime: String = "",
    @ColumnInfo(name = "riskAssessment")
    val tripRiskAssessment: String = "",
    @ColumnInfo(name = "description")
    val tripDescription: String = "",
)
