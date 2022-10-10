package com.example.mexpense

import android.app.Application
import com.example.mexpense.data.trip.TripRoomDatabase

class MExpenseApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database: TripRoomDatabase by lazy { TripRoomDatabase.getDatabase(this) }
}