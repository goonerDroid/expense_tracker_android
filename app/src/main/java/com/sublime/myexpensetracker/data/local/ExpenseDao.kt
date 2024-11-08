package com.sublime.myexpensetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sublime.myexpensetracker.data.models.Expense

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expense WHERE createdBy = :userId")
    suspend fun getExpenseForUser(userId: String): List<Expense>

    @Query(
        """
        SELECT e.* FROM expense e
        INNER JOIN expensesplit es ON e.expenseId = es.expenseId
        WHERE es.userId = :userId
    """,
    )
    suspend fun getExpenseSHaredWithUser(userId: String): List<Expense>
}
