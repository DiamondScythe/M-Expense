package com.example.mexpense.data.expense

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val expenseId: Int = 0,
    @ColumnInfo(name = "name")
    val expenseName: String = "",
    @ColumnInfo(name = "details")
    val expenseDetails: String = "",
    @ColumnInfo(name = "type")
    val expenseType: String = "",
    @ColumnInfo(name = "amount")
    val expenseAmount: Int = 0,
    @ColumnInfo(name = "tripOwnerId")
    val tripOwnerId: Int = 0
)