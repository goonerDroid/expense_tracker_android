package com.sublime.myexpensetracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sublime.myexpensetracker.data.models.User
import com.sublime.myexpensetracker.ui.viewmodels.UserManagementViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun UserSelectionScreen(
    viewModel: UserManagementViewModel,
    onUserSelected: (User) -> Unit,
) {
    var showNewUserDialog by remember { mutableStateOf(false) }
    var newUserName by remember { mutableStateOf("") }
    val users by viewModel.users.collectAsState()

    // Add LaunchedEffect to load users when screen is created
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Text(
            text = "Select or create user",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        // Show existing users
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(users) { user ->
                UserCard(
                    user = user,
                    onClick = { onUserSelected(user) },
                )
            }
        }

        Button(
            onClick = { showNewUserDialog = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Create new user")
        }
    }

    if (showNewUserDialog) {
        AlertDialog(
            onDismissRequest = { showNewUserDialog = false },
            title = { Text("Create New User") },
            text = {
                TextField(
                    value = newUserName,
                    onValueChange = { newUserName = it },
                    label = { Text("Name") },
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newUserName.isNotBlank()) {
                            viewModel.createUser(newUserName)
                            showNewUserDialog = false
                        }
                    },
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewUserDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }
}
