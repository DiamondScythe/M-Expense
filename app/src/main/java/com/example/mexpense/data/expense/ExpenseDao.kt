package com.example.mexpense.data.expense

import androidx.room.*
import com.example.mexpense.data.trip.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(delete: Expense)

    @Query("SELECT * from expense WHERE id = :id")
    fun getTrip(id: Int): Flow<Expense>

    @Query("SELECT * from expense ORDER BY name ASC")
    fun getTrips(): Flow<List<Expense>>
}