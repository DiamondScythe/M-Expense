package com.example.mexpense

import android.content.ClipData
import android.telephony.TelephonyCallback.CellLocationListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mexpense.data.Trip
import com.example.mexpense.data.TripDao
import kotlinx.coroutines.launch

class TripViewModel(private val tripDao: TripDao) : ViewModel() {
    private fun insertTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.insert(trip)
        }
    }

    private fun getNewTripEntry(tripLocation: String, tripTime: String): Trip {
        return Trip(
            tripLocation = tripLocation,
            tripTime = tripTime
        )
    }

    fun addNewTrip(tripLocation: String, tripTime: String) {
        val newTrip = getNewTripEntry(tripLocation, tripTime)
        insertTrip(newTrip)
    }

    //check if the input box is empty or not
    fun isEntryValid(tripLocation: String, tripTime: String): Boolean {
        if (tripLocation.isBlank() || tripTime.isBlank()) {
            return false
        }
        return true
    }

}

class TripViewModelFactory(private val tripDao: TripDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //Check if the modelClass is the same as the InventoryViewModel class and return an instance
        //of it. Otherwise, throw an exception.
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(tripDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
