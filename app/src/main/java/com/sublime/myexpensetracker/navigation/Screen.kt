package com.sublime.myexpensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sublime.myexpensetracker.data.models.User
import com.sublime.myexpensetracker.ui.components.CreateExpenseScreen
import com.sublime.myexpensetracker.ui.components.ExpenseListScreen
import com.sublime.myexpensetracker.ui.components.UserSelectionScreen
import com.sublime.myexpensetracker.ui.viewmodels.ExpenseViewModel
import com.sublime.myexpensetracker.ui.viewmodels.UserManagementViewModel

sealed class Screen(
    val route: String,
) {
    data object UserSelection : Screen("user_selection")

    data object ExpenseList : Screen("expense_list")

    data object CreateExpense : Screen("create_expense")
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ExpenseSharingNavigation() {
    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf<User?>(null) }

    NavHost(
        navController = navController,
        startDestination = Screen.UserSelection.route,
    ) {
        composable(Screen.UserSelection.route) {
            val viewModel: UserManagementViewModel = hiltViewModel()
            UserSelectionScreen(
                viewModel = viewModel,
                onUserSelected = { user ->
                    currentUser = user
                    navController.navigate(Screen.ExpenseList.route)
                },
            )
        }

        composable(Screen.ExpenseList.route) {
            val viewModel: ExpenseViewModel = hiltViewModel()
            ExpenseListScreen(
                viewModel = viewModel,
                userId = currentUser?.userId ?: "",
                onCreateExpenseClick = {
                    navController.navigate(Screen.CreateExpense.route)
                },
            )
        }

        composable(Screen.CreateExpense.route) {
            val expenseViewModel: ExpenseViewModel = hiltViewModel()
            val userManagementViewModel: UserManagementViewModel = hiltViewModel()
            CreateExpenseScreen(
                userManagementViewModel,
                expenseViewModel,
                currentUserId = currentUser?.userId ?: "",
                onExpenseCreated = {
                    navController.popBackStack()
                },
            )
        }
    }
}
