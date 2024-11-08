package com.sublime.myexpensetracker.data.models

data class ExpenseWithSplits(
    val expense: Expense,
    val splits: List<ExpenseSplit>,
)
