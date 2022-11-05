package com.example.mexpense.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.data.expense.ExpenseDao
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.data.trip.TripDao

class BackupViewModel(private val tripDao: TripDao, private val expenseDao: ExpenseDao): ViewModel(){

    val allTrips: List<Trip> = tripDao.getStaticTrips()
    val allExpenses: List<Expense> = expenseDao.getStaticExpenses()

    fun returnBackupItems(): Pair<List<Trip>, List<Expense>>{
        return Pair(allTrips, allExpenses)
    }

}