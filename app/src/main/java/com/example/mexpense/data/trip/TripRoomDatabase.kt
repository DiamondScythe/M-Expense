package com.example.mexpense.data.trip

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Trip::class], version = 6, exportSchema = false)
abstract class TripRoomDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    companion object {
        //The INSTANCE variable will keep a reference to the database, when one has been created.
        //This helps in maintaining a single instance of the database opened at any given time.
        @Volatile
        private var INSTANCE: TripRoomDatabase? = null
        fun getDatabase(context: Context): TripRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripRoomDatabase::class.java,
                    "trip_database"
                )   // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}