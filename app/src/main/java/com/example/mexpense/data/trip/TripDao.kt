package com.example.mexpense.data.trip

import androidx.room.*
import com.example.mexpense.data.trip.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trip: Trip)

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    //co flow khoi xai suspend
    @Query("SELECT * from trip WHERE id = :id")
    fun getTrip(id: Int): Flow<Trip>

    @Query("SELECT * from trip ORDER BY location ASC")
    fun getTrips(): Flow<List<Trip>>

//    @Transaction
//    @Query("SELECT * FROM trip")
//    fun getUsersWithPlaylists(): List<TripWithExpenses>
}