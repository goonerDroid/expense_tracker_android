package com.sublime.myexpensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sublime.myexpensetracker.data.models.Expense
import com.sublime.myexpensetracker.data.models.ExpenseSplit
import com.sublime.myexpensetracker.data.models.ExpenseWithSplits
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("ktlint:standard:function-naming")
@Composable
fun ExpenseCard(
    expenseWithSplits: ExpenseWithSplits,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            // Header: Description and Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = expenseWithSplits.expense.description,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = "₹${expenseWithSplits.expense.amount}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = formatDate(expenseWithSplits.expense.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Divider
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Splits Section
            Text(
                text = "Split Details",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // List of splits
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                expenseWithSplits.splits.forEach { split ->
                    SplitItem(split = split)
                }
            }
        }
    }
}

@Composable
private fun SplitItem(
    split: ExpenseSplit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            // Status indicator
            Box(
                modifier =
                    Modifier
                        .size(8.dp)
                        .background(
                            color =
                                if (split.paid) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.error
                                },
                            shape = CircleShape,
                        ),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = split.userId, // You might want to show user name instead
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Text(
            text = "₹${split.amount}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// Utility function to format timestamp
private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

// Preview
@Preview(showBackground = true)
@Composable
fun ExpenseCardPreview() {
    MaterialTheme {
        ExpenseCard(
            expenseWithSplits =
                ExpenseWithSplits(
                    expense =
                        Expense(
                            expenseId = "1",
                            description = "Team Lunch",
                            amount = BigDecimal("1000.00"),
                            createdBy = "user1",
                            createdAt = System.currentTimeMillis(),
                        ),
                    splits =
                        listOf(
                            ExpenseSplit(
                                splitId = "1",
                                expenseId = "1",
                                userId = "John",
                                amount = BigDecimal("250.00"),
                                paid = true,
                            ),
                            ExpenseSplit(
                                splitId = "2",
                                expenseId = "1",
                                userId = "Alice",
                                amount = BigDecimal("250.00"),
                                paid = false,
                            ),
                            ExpenseSplit(
                                splitId = "3",
                                expenseId = "1",
                                userId = "Bob",
                                amount = BigDecimal("250.00"),
                                paid = true,
                            ),
                            ExpenseSplit(
                                splitId = "4",
                                expenseId = "1",
                                userId = "Carol",
                                amount = BigDecimal("250.00"),
                                paid = false,
                            ),
                        ),
                ),
        )
    }
}
