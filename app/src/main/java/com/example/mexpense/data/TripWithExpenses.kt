package com.example.mexpense.data

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.data.trip.Trip

data class TripWithExpenses(
    @Embedded val trip: Trip,
    @Relation(
        parentColumn = "id",
        entityColumn = "tripOwnerId"
    )
    val expenses: List<Expense>
)