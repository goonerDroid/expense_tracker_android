package com.sublime.myexpensetracker.ui.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sublime.myexpensetracker.data.models.User
import com.sublime.myexpensetracker.ui.viewmodels.ExpenseViewModel
import com.sublime.myexpensetracker.ui.viewmodels.UserManagementViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Suppress("ktlint:standard:function-naming")
@Composable
fun CreateExpenseScreen(
    userManagementViewModel: UserManagementViewModel,
    expenseViewModel: ExpenseViewModel,
    currentUserId: String,
    onExpenseCreated: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedUsers by remember { mutableStateOf<Set<User>>(emptySet()) }
    var showUserSelectionDialog by remember { mutableStateOf(false) }

    val users by userManagementViewModel.users.collectAsState()
    val context = LocalContext.current

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
    ) {
        // Header
        Text(
            text = "Create New Expense",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        // Description Field
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("e.g., Team Lunch") },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
        )

        // Amount Field
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            placeholder = { Text("₹0.00") },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                ),
            singleLine = true,
            prefix = { Text("₹") },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
        )

        // Split With Section
        Text(
            text = "Split With",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        // Selected Users Display
        if (selectedUsers.isNotEmpty()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
            ) {
                selectedUsers.forEach { user ->
                    SelectedUserChip(
                        user = user,
                        onRemove = {
                            selectedUsers = selectedUsers - user
                        },
                    )
                }
            }
        }

        // Add People Button
        OutlinedButton(
            onClick = { showUserSelectionDialog = true },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add People",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add People")
        }

        // Split Preview
        if (selectedUsers.isNotEmpty() && amount.isNotBlank()) {
            SplitPreviewCard(
                totalAmount = amount.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                numberOfPeople = selectedUsers.size + 1, // +1 for current user
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Create Expense Button
        Button(
            onClick = {
                val amountValue = amount.toBigDecimalOrNull()
                when {
                    description.isBlank() -> {
                        Toast
                            .makeText(context, "Please enter a description", Toast.LENGTH_SHORT)
                            .show()
                    }

                    amountValue == null || amountValue <= BigDecimal.ZERO -> {
                        Toast
                            .makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT)
                            .show()
                    }

                    selectedUsers.isEmpty() -> {
                        Toast
                            .makeText(
                                context,
                                "Please select at least one person to split with",
                                Toast.LENGTH_SHORT,
                            ).show()
                    }

                    else -> {
                        expenseViewModel.createExpense(
                            description = description,
                            amount = amountValue,
                            createdByUserId = currentUserId,
                            participants = selectedUsers.toList(),
                        )
                        onExpenseCreated()
                    }
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            enabled = description.isNotBlank() && amount.isNotBlank() && selectedUsers.isNotEmpty(),
        ) {
            Text("Create Expense")
        }
    }

    // User Selection Dialog
    if (showUserSelectionDialog) {
        UserSelectionDialog(
            users = users.filter { it.userId != currentUserId },
            selectedUsers = selectedUsers,
            onUserSelected = { selectedUsers = it },
            onDismiss = { showUserSelectionDialog = false },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun SelectedUserChip(
    user: User,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove ${user.name}",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun SplitPreviewCard(
    totalAmount: BigDecimal,
    numberOfPeople: Int,
    modifier: Modifier = Modifier,
) {
    val amountPerPerson =
        totalAmount.divide(
            BigDecimal(numberOfPeople),
            2,
            RoundingMode.HALF_UP,
        )

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(
                text = "Split Preview",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Amount per person",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "₹$amountPerPerson",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserSelectionDialog(
    users: List<User>,
    selectedUsers: Set<User>,
    onUserSelected: (Set<User>) -> Unit,
    onDismiss: () -> Unit,
) {
    var tempSelectedUsers by remember { mutableStateOf(selectedUsers) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select People") },
        text = {
            LazyColumn {
                items(users) { user ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    tempSelectedUsers =
                                        if (user in tempSelectedUsers) {
                                            tempSelectedUsers - user
                                        } else {
                                            tempSelectedUsers + user
                                        }
                                }.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Checkbox(
                            checked = user in tempSelectedUsers,
                            onCheckedChange = { checked ->
                                tempSelectedUsers =
                                    if (checked) {
                                        tempSelectedUsers + user
                                    } else {
                                        tempSelectedUsers - user
                                    }
                            },
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onUserSelected(tempSelectedUsers)
                onDismiss()
            }) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
