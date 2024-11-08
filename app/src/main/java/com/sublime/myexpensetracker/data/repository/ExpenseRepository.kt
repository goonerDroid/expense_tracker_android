package com.sublime.myexpensetracker.data.repository

import com.sublime.myexpensetracker.data.local.ExpenseDao
import com.sublime.myexpensetracker.data.local.ExpenseSplitDao
import com.sublime.myexpensetracker.data.local.UserDao
import com.sublime.myexpensetracker.data.models.Expense
import com.sublime.myexpensetracker.data.models.ExpenseSplit
import com.sublime.myexpensetracker.data.models.ExpenseWithSplits
import com.sublime.myexpensetracker.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository
    @Inject
    constructor(
        private val expenseDao: ExpenseDao,
        private val expenseSplitDao: ExpenseSplitDao,
        private val userDao: UserDao,
    ) {
        suspend fun createExpense(
            description: String,
            amount: BigDecimal,
            createdByUserId: String,
            splits: List<ExpenseSplit>,
        ) {
            val expense =
                Expense(
                    expenseId = UUID.randomUUID().toString(),
                    description = description,
                    amount = amount,
                    createdBy = createdByUserId,
                    createdAt = System.currentTimeMillis(),
                )

            expenseDao.insertExpense(expense)
            splits.forEach { split ->
                expenseSplitDao.insertExpenseSplit(split.copy(expenseId = expense.expenseId))
            }
        }

        suspend fun getExpensesForUser(userId: String): Flow<List<ExpenseWithSplits>> =
            combine(
                flow { emit(expenseDao.getExpenseForUser(userId)) },
                flow { emit(expenseDao.getExpenseSharedWithUser(userId)) },
            ) { created, shared ->
                (created + shared).distinct().map { expense ->
                    ExpenseWithSplits(
                        expense = expense,
                        splits = expenseSplitDao.getSplitsForExpense(expense.expenseId),
                    )
                }
            }

        suspend fun getAllUsers(): List<User> = userDao.getAllUsers()

        suspend fun createUser(name: String): User {
            val user =
                User(
                    userId = UUID.randomUUID().toString(),
                    name = name,
                )
            userDao.insertUser(user)
            return user
        }
    }
