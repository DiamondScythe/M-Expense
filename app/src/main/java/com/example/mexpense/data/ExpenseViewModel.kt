package com.example.mexpense.data

import androidx.lifecycle.*
import com.example.mexpense.TripViewModel
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.data.expense.ExpenseDao
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.data.trip.TripDao
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseDao: ExpenseDao): ViewModel() {
    val allExpenses: LiveData<List<Expense>> = expenseDao.getExpenses().asLiveData()

    private fun insertExpense(expense: Expense){
        viewModelScope.launch {
            expenseDao.insert(expense)
        }
    }

    private fun getNewExpenseEntry(
        expenseName: String, expenseDetails: String, expenseType: String,
        expenseAmount: Int, tripOwnerId: Int): Expense {
        return Expense(
            expenseName = expenseName,
            expenseDetails = expenseDetails,
            expenseType = expenseType,
            expenseAmount = expenseAmount,
            tripOwnerId = tripOwnerId
        )
    }

    fun isEntryValid(
        expenseName: String, expenseDetails: String, expenseType: String,
        expenseAmount: Int, tripOwnerId: Int): Boolean {
        if (
            expenseName.isBlank() || expenseDetails.isBlank() || expenseType.isBlank() ||
        expenseAmount == 0 || tripOwnerId == 0
        ) {
            return false
        }
        return true
    }

    fun addNewExpense(expenseName: String, expenseDetails: String, expenseType: String,
                      expenseAmount: Int, tripOwnerId: Int){
        val newExpense = getNewExpenseEntry(expenseName, expenseDetails, expenseType,
            expenseAmount, tripOwnerId)
    }
}

class ExpenseViewModelFactory(private val expenseDao: ExpenseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //Check if the modelClass is the same as the InventoryViewModel class and return an instance
        //of it. Otherwise, throw an exception.
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(expenseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}