package com.sublime.myexpensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sublime.myexpensetracker.data.models.ExpenseSplit
import com.sublime.myexpensetracker.data.models.ExpenseWithSplits
import com.sublime.myexpensetracker.data.models.User
import com.sublime.myexpensetracker.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel
    @Inject
    constructor(
        private val expenseRepository: ExpenseRepository,
    ) : ViewModel() {
        private val _expenses = MutableStateFlow<List<ExpenseWithSplits>>(emptyList())
        val expenses: StateFlow<List<ExpenseWithSplits>> = _expenses.asStateFlow()

        fun createExpense(
            description: String,
            amount: BigDecimal,
            createdByUserId: String,
            participants: List<User>,
        ) {
            viewModelScope.launch {
                val splitAmount = amount.divide(BigDecimal(participants.size))
                val splits =
                    participants.map { user ->
                        ExpenseSplit(
                            splitId = UUID.randomUUID().toString(),
                            expenseId = "", // expense id is being set inside the repository
                            userId = user.userId,
                            amount = splitAmount,
                        )
                    }

                expenseRepository.createExpense(
                    description = description,
                    amount = amount,
                    createdByUserId = createdByUserId,
                    splits = splits,
                )
            }
        }

        fun loadExpensesForUser(userId: String) {
            viewModelScope.launch {
                expenseRepository.getExpensesForUser(userId).collect { expensesList ->
                    _expenses.value = expensesList
                }
            }
        }
    }
