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
    fun getExpense(id: Int): Flow<Expense>

    @Query("SELECT * from expense ORDER BY name ASC")
    fun getExpenses(): Flow<List<Expense>>

    @Query("SELECT * from expense")
    fun getStaticExpenses(): List<Expense>

    @Query("SELECT * from expense WHERE tripOwnerId = :tripId")
    fun getTripExpenses(tripId: Int): Flow<List<Expense>>

    @Query("SELECT tripOwnerId from expense WHERE id = :id")
    fun getTripOwnerId(id: Int): Int

    @Query("SELECT type from expense WHERE id = :id")
    fun getExpenseType(id: Int): String

    @Query("SELECT * from expense WHERE id = :id")
    fun getStaticExpense(id: Int): Expense

    @Query("DELETE from expense where id = :id")
    fun delete2(id: Int)

    @Query("DELETE FROM expense")
    fun dropExpense()
}