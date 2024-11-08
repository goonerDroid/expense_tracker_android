package com.sublime.myexpensetracker.data.models

import androidx.room.PrimaryKey
import java.math.BigDecimal

data class Expense(
    @PrimaryKey val expenseId: String,
    val description: String,
    val amount: BigDecimal,
    val createdBy: String,
    val createdAt: Long,
)
