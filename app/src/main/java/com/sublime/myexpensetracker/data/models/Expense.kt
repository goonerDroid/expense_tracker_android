package com.sublime.myexpensetracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class Expense(
    @PrimaryKey val expenseId: String,
    val description: String,
    val amount: BigDecimal,
    val createdBy: String,
    val createdAt: Long,
)
