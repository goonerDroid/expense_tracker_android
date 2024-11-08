package com.sublime.myexpensetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sublime.myexpensetracker.data.models.ExpenseSplit

@Dao
interface ExpenseSplitDao {
    @Insert
    suspend fun insertExpenseSplit(expenseSplit: ExpenseSplit)

    @Query("SELECT * FROM expensesplit WHERE expenseId = :expenseId")
    suspend fun getSplitsForExpense(expenseId: String): List<ExpenseSplit>
}
