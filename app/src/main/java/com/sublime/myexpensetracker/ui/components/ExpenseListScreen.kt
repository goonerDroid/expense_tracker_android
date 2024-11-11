package com.sublime.myexpensetracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sublime.myexpensetracker.ui.viewmodels.ExpenseViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun ExpenseListScreen(
    viewModel: ExpenseViewModel,
    userId: String,
    userName: String, // Add userName parameter
    onCreateExpenseClick: () -> Unit,
) {
    val expenses by viewModel.expenses.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadExpensesForUser(userId)
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        // Add user name at top
        Text(
            text = "Welcome, $userName",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Button(
            onClick = onCreateExpenseClick,
            modifier = Modifier.align(Alignment.End),
        ) {
            Text("Create Expense")
        }

        LazyColumn {
            items(expenses) { expenseWithSplits ->
//                ExpenseCard(
//                    expenseWithSplits = expenseWithSplits,
//                    users = viewModel.users.value, // Pass users list
//                )
            }
        }
    }
}
