package com.sublime.myexpensetracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class ExpenseSplit(
    @PrimaryKey val splitId: String,
    val expenseId: String,
    val userId: String,
    val amount: BigDecimal,
    val paid: Boolean = false,
)
