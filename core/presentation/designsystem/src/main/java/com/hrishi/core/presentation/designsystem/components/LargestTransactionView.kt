package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.primaryFixed

@Composable
fun LargestTransactionView(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    amount: String = "",
    date: String = "",
    emptyTransactionsText: String = "Your largest transaction will appear here",
    isEmptyTransactions: Boolean = false,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
            .background(
                color = primaryFixed,
                shape = RoundedCornerShape(16.dp)
            )
            .defaultMinSize(minHeight = 72.dp)
            .padding(12.dp)
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (isEmptyTransactions) {
            EmptyTransactionContent(emptyTransactionsText)
        } else {
            TransactionDetails(
                title = title,
                description = description,
                amount = amount,
                date = date
            )
        }
    }
}

@Composable
private fun EmptyTransactionContent(emptyTransactionsText: String) {
    Text(
        text = emptyTransactionsText,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TransactionDetails(
    title: String,
    description: String,
    amount: String,
    date: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TransactionInfoColumn(
            modifier = Modifier.weight(1f),
            title = title,
            description = description
        )

        Spacer(modifier = Modifier.width(24.dp))

        TransactionAmountColumn(
            amount = amount,
            date = date
        )
    }
}

@Composable
private fun TransactionInfoColumn(
    modifier: Modifier = Modifier,
    title: String,
    description: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        )
    }
}

@Composable
private fun TransactionAmountColumn(
    amount: String,
    date: String
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = amount,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = date,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLargestTransactionViewStates() {
    SpendLessFinanceTrackerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LargestTransactionView(
                    title = "Adobe Yearly",
                    description = "Largest transaction",
                    amount = "-\$59.99",
                    date = "Jan 7, 2025",
                    isEmptyTransactions = false
                )

                LargestTransactionView(
                    isEmptyTransactions = true
                )
            }
        }
    }
}