package com.example.mexpense

import android.content.ClipData
import android.content.LocusId
import androidx.lifecycle.*
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.data.trip.TripDao
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class TripViewModel(private val tripDao: TripDao) : ViewModel() {

    val allTrips: LiveData<List<Trip>> = tripDao.getTrips().asLiveData()

    private fun insertTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.insert(trip)
        }
    }

    private fun updateTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.update(trip)
        }
    }

    private fun deleteTrip(tripId: Int){
        viewModelScope.launch {
            tripDao.delete2(tripId)
        }
    }

    private fun getNewTripEntry(tripLocation: String, tripTime: String,
                                tripRiskAssessment: String, tripDescription: String): Trip {
        return Trip(
            tripLocation = tripLocation,
            tripTime = tripTime,
            tripRiskAssessment = tripRiskAssessment,
            tripDescription = tripDescription
        )
    }

    private fun getNewTripEditEntry(tripId: Int, tripLocation: String, tripTime: String,
                                tripRiskAssessment: String, tripDescription: String): Trip {
        return Trip(
            tripId = tripId,
            tripLocation = tripLocation,
            tripTime = tripTime,
            tripRiskAssessment = tripRiskAssessment,
            tripDescription = tripDescription
        )
    }

    fun addNewTrip(tripLocation: String, tripTime: String,
                   tripRiskAssessment: String, tripDescription: String) {
        val newTrip = getNewTripEntry(tripLocation, tripTime, tripRiskAssessment, tripDescription)
        insertTrip(newTrip)
    }

    fun updateWithNewTrip(tripId: Int, tripLocation: String, tripTime: String,
                 tripRiskAssessment: String, tripDescription: String) {
        val newTrip = getNewTripEditEntry(tripId, tripLocation, tripTime, tripRiskAssessment, tripDescription)
        updateTrip(newTrip)
    }

    //check if the input box is empty or not
    fun isEntryValid(tripLocation: String, tripTime: String, tripRiskAssessment: String): Boolean {
        if (tripLocation.isBlank() || tripTime.isBlank() || tripRiskAssessment.isBlank()) {
            return false
        }
        return true
    }

    fun retrieveTrip(id: Int): LiveData<Trip> {
        return tripDao.getTrip(id).asLiveData()
    }

    fun retrieveStaticTrip(id: Int): Trip {
        return tripDao.getStaticTrip(id)
    }

    fun removeTrip(tripId: Int){
        return tripDao.delete2(tripId)
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
