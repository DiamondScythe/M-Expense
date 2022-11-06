package com.example.mexpense.data

import androidx.lifecycle.*
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.data.expense.ExpenseDao
import com.example.mexpense.data.trip.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseDao: ExpenseDao): ViewModel() {
    val allExpenses: LiveData<List<Expense>> = expenseDao.getExpenses().asLiveData()

    private fun insertExpense(expense: Expense){
        viewModelScope.launch {
            expenseDao.insert(expense)
        }
    }

    private fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.update(expense)
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

    fun retrieveTripExpense(tripOwnerId: Int): LiveData<List<Expense>> {
        return expenseDao.getTripExpenses(tripOwnerId).asLiveData()
    }

    fun retrieveTripOwnerId(id: Int): Int{
        return expenseDao.getTripOwnerId(id)
    }

    fun isEntryValid(
        expenseName: String, expenseDetails: String,
        expenseAmount: Int, tripOwnerId: Int): Boolean {
        if (
            expenseName.isBlank() || expenseDetails.isBlank() ||
        expenseAmount == 0 || tripOwnerId == 0
        ) {
            return false
        }
        return true
    }

    fun retrieveStaticExpenses(): List<Expense>{
        return expenseDao.getStaticExpenses()
    }

    fun retrieveStaticExpense(expenseId: Int): Expense{
        return expenseDao.getStaticExpense(expenseId)
    }

    fun addNewExpense(expenseName: String, expenseDetails: String, expenseType: String,
                      expenseAmount: Int, tripOwnerId: Int){
        val newExpense = getNewExpenseEntry(expenseName, expenseDetails, expenseType,
            expenseAmount, tripOwnerId)
        insertExpense(newExpense)
    }

    fun retrieveExpense(expenseId: Int): LiveData<Expense> {
        return expenseDao.getExpense(expenseId).asLiveData()
    }

    fun updateWithNewExpense(
        expenseId: Int, expenseName: String, expenseDetails: String, expenseType: String,
        expenseAmount: Int, tripOwnerId: Int){
        val newExpense = getNewExpenseEditEntry(
            expenseId, expenseName, expenseDetails, expenseType, expenseAmount, tripOwnerId)
        updateExpense(newExpense)
    }

    private fun getNewExpenseEditEntry(
        expenseId: Int, expenseName: String, expenseDetails: String, expenseType: String, expenseAmount: Int, tripOwnerId: Int): Expense {
        return Expense(
            expenseId = expenseId,
            expenseName = expenseName,
            expenseDetails = expenseDetails,
            expenseType = expenseType,
            expenseAmount = expenseAmount,
            tripOwnerId = tripOwnerId
        )
    }

    fun retrieveExpenseType(expenseId: Int): String {
        return expenseDao.getExpenseType(expenseId)
    }

    fun removeExpense(expenseId: Int) {
        expenseDao.delete2(expenseId)
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