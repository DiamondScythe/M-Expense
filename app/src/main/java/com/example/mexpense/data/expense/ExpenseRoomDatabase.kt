package com.example.mexpense.data.expense

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class ExpenseRoomDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    companion object {
        //The INSTANCE variable will keep a reference to the database, when one has been created.
        //This helps in maintaining a single instance of the database opened at any given time.
        @Volatile
        private var INSTANCE: ExpenseRoomDatabase? = null
        fun getDatabase(context: Context): ExpenseRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseRoomDatabase::class.java,
                    "expense_database"
                )   // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}