package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrishi.core.presentation.designsystem.NoteIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.primaryFixed
import com.hrishi.core.presentation.designsystem.secondaryFixed
import com.hrishi.core.presentation.designsystem.secondaryFixedDim
import com.hrishi.core.presentation.designsystem.success

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    category: String,
    note: String? = null,
    amount: Double,
    amountColor: Color = if (amount > 0) success else MaterialTheme.colorScheme.onSurface,
    isCollapsed: Boolean = true,
    noteIcon: ImageVector = NoteIcon,
    expenseBackgroundColor: Color = primaryFixed,
    incomeBackgroundColor: Color = secondaryFixed.copy(alpha = 0.30f),
    collapsedNoteExpenseColor: Color = MaterialTheme.colorScheme.inversePrimary,
    expandedNoteExpenseColor: Color = MaterialTheme.colorScheme.primaryContainer,
    collapsedNoteIncomeColor: Color = secondaryFixedDim,
    expandedNoteIncomeColor: Color = MaterialTheme.colorScheme.secondary,
    displayAmount: (Double) -> String,
    onCardClicked: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(!isCollapsed) }
    val canExpand = !note.isNullOrBlank()

    // Apply the same shape & padding in both states, so items don't shift.
    // Only toggle the background color if expanded.
    val containerModifier = modifier
        .clickable(enabled = canExpand) {
            // If there's no note, no expand/collapse logic applies
            if (canExpand) {
                expanded = !expanded
                onCardClicked(expanded)
            }
        }
        .background(
            color = if (expanded && canExpand) {
                MaterialTheme.colorScheme.surfaceContainerLowest
            } else {
                Color.Transparent
            },
            shape = RoundedCornerShape(16.dp)
        )
        .padding(4.dp)
        .padding(end = 2.dp)

    val expenseIncomeBackgroundColor = if (amount > 0) {
        incomeBackgroundColor
    } else {
        expenseBackgroundColor
    }
    val collapsedNoteColor = if (amount > 0) {
        collapsedNoteIncomeColor
    } else {
        collapsedNoteExpenseColor
    }
    val expandedNoteColor = if (amount > 0) {
        expandedNoteIncomeColor
    } else {
        expandedNoteExpenseColor
    }

    Box(modifier = containerModifier) {
        TransactionItemInnerContent(
            modifier = Modifier,
            expenseIncomeBackgroundColor = expenseIncomeBackgroundColor,
            icon = icon,
            note = note,
            collapsedNoteColor = collapsedNoteColor,
            expandedNoteColor = expandedNoteColor,
            noteIcon = noteIcon,
            title = title,
            category = category,
            amount = amount,
            amountColor = amountColor,
            isCollapsed = !expanded,
            displayAmount = displayAmount
        )
    }
}

@Composable
private fun TransactionItemInnerContent(
    modifier: Modifier = Modifier,
    expenseIncomeBackgroundColor: Color,
    icon: String,
    note: String?,
    collapsedNoteColor: Color,
    expandedNoteColor: Color,
    noteIcon: ImageVector,
    title: String,
    category: String,
    amount: Double,
    amountColor: Color,
    isCollapsed: Boolean,
    displayAmount: (Double) -> String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Icon + optional note indicator
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(Color.Transparent)
        ) {
            // Colored background for expense or income
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(44.dp)
                    .background(
                        color = expenseIncomeBackgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            // Main icon text
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = icon
            )

            // If there's a note, draw a small overlay icon
            if (!note.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            shape = RoundedCornerShape(6.dp)
                        )
                ) {
                    Icon(
                        tint = if (isCollapsed) collapsedNoteColor else expandedNoteColor,
                        modifier = Modifier.align(Alignment.Center),
                        imageVector = noteIcon,
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = title,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = category,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = displayAmount(amount),
                    style = MaterialTheme.typography.titleLarge.copy(color = amountColor)
                )
            }

            // Only show the note if not collapsed
            if (!note.isNullOrBlank() && !isCollapsed) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private val defaultAmountFormatter: (Double) -> String = {
    "$${String.format("%.2f", it)}"
}

@Preview(name = "Multiple Scenarios", showBackground = true)
@Composable
fun PreviewTransactionItem() {
    SpendLessFinanceTrackerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1) Collapsed with note
            TransactionItem(
                icon = "🍔",
                title = "Burger King",
                category = "Food",
                note = "Grabbed a quick lunch",
                amount = 8.50,
                isCollapsed = true,
                displayAmount = defaultAmountFormatter,
                onCardClicked = {}
            )

            // 2) Expanded with note
            TransactionItem(
                icon = "⚽",
                title = "Football tickets",
                category = "Sports",
                note = "Went to a local match with friends",
                amount = 45.00,
                isCollapsed = false,
                displayAmount = defaultAmountFormatter,
                onCardClicked = {}
            )

            // 3) Collapsed with no note
            TransactionItem(
                icon = "🚗",
                title = "Parking",
                category = "Transport",
                note = null,
                amount = 2.00,
                isCollapsed = true,
                displayAmount = defaultAmountFormatter,
                onCardClicked = {}
            )

            // 4) Negative amount (expense) with note, collapsed
            TransactionItem(
                icon = "📱",
                title = "Phone Bill",
                category = "Utilities",
                note = "Monthly charge for phone services",
                amount = -49.99,
                isCollapsed = true,
                displayAmount = defaultAmountFormatter,
                onCardClicked = {}
            )

            // 5) Negative amount (expense) but expanded
            TransactionItem(
                icon = "💻",
                title = "Laptop Payment",
                category = "Electronics",
                note = "Installment for new MacBook",
                amount = -999.99,
                isCollapsed = false,
                displayAmount = defaultAmountFormatter,
                onCardClicked = {}
            )
        }
    }
}