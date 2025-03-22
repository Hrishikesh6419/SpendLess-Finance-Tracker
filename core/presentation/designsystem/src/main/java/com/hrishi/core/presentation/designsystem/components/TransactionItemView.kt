package com.hrishi.core.presentation.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun TransactionItemView(
    modifier: Modifier = Modifier,
    icon: String,
    incomeIcon: String = "\uD83D\uDCB0",
    title: String,
    category: String,
    note: String? = null,
    amount: BigDecimal,
    incomeAmountColor: Color = success,
    expenseAmountColor: Color = MaterialTheme.colorScheme.onSurface,
    isCollapsed: Boolean = true,
    noteIcon: ImageVector = NoteIcon,
    expenseBackgroundColor: Color = primaryFixed,
    incomeBackgroundColor: Color = secondaryFixed.copy(alpha = 0.30f),
    collapsedNoteExpenseColor: Color = MaterialTheme.colorScheme.inversePrimary,
    expandedNoteExpenseColor: Color = MaterialTheme.colorScheme.primaryContainer,
    collapsedNoteIncomeColor: Color = secondaryFixedDim,
    expandedNoteIncomeColor: Color = MaterialTheme.colorScheme.secondary,
    displayAmount: (BigDecimal) -> String,
    onCardClicked: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(!isCollapsed) }
    val canExpand = !note.isNullOrBlank()

    val containerModifier = modifier
        .clickable(enabled = canExpand) {
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

    val amountColor = if (amount.compareTo(BigDecimal.ZERO) > 0) {
        incomeAmountColor
    } else {
        expenseAmountColor
    }

    val finalIcon = if (amount.compareTo(BigDecimal.ZERO) > 0) {
        incomeIcon
    } else {
        icon
    }

    val expenseIncomeBackgroundColor = if (amount.compareTo(BigDecimal.ZERO) > 0) {
        incomeBackgroundColor
    } else {
        expenseBackgroundColor
    }
    val collapsedNoteColor = if (amount.compareTo(BigDecimal.ZERO) > 0) {
        collapsedNoteIncomeColor
    } else {
        collapsedNoteExpenseColor
    }
    val expandedNoteColor = if (amount.compareTo(BigDecimal.ZERO) > 0) {
        expandedNoteIncomeColor
    } else {
        expandedNoteExpenseColor
    }

    Box(modifier = containerModifier) {
        TransactionItemInnerContent(
            modifier = Modifier,
            expenseIncomeBackgroundColor = expenseIncomeBackgroundColor,
            icon = finalIcon,
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
    amount: BigDecimal,
    amountColor: Color,
    isCollapsed: Boolean,
    displayAmount: (BigDecimal) -> String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(44.dp)
                    .background(
                        color = expenseIncomeBackgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = icon
            )

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

            AnimatedVisibility(
                modifier = Modifier.padding(top = 6.dp),
                visible = !note.isNullOrBlank() && !isCollapsed,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Text(
                    text = note.orEmpty(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
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
            TransactionItemView(
                icon = "🍔",
                title = "Burger King",
                category = "Food",
                note = "Grabbed a quick lunch",
                amount = BigDecimal("8.50"),
                isCollapsed = true,
                displayAmount = fakeDefaultAmountFormatter,
                onCardClicked = {}
            )

            // 2) Expanded with note
            TransactionItemView(
                icon = "⚽",
                title = "Football tickets",
                category = "Sports",
                note = "Went to a local match with friends",
                amount = BigDecimal("45.00"),
                isCollapsed = false,
                displayAmount = fakeDefaultAmountFormatter,
                onCardClicked = {}
            )

            // 3) Collapsed with no note
            TransactionItemView(
                icon = "🚗",
                title = "Parking",
                category = "Transport",
                note = null,
                amount = BigDecimal("2.00"),
                isCollapsed = true,
                displayAmount = fakeDefaultAmountFormatter,
                onCardClicked = {}
            )

            // 4) Negative amount (expense) with note, collapsed
            TransactionItemView(
                icon = "📱",
                title = "Phone Bill",
                category = "Utilities",
                note = "Monthly charge for phone services",
                amount = BigDecimal("-49.99"),
                isCollapsed = true,
                displayAmount = fakeDefaultAmountFormatter,
                onCardClicked = {}
            )

            // 5) Negative amount (expense) but expanded
            TransactionItemView(
                icon = "💻",
                title = "Laptop Payment",
                category = "Electronics",
                note = "Installment for new MacBook",
                amount = BigDecimal("-999.99"),
                isCollapsed = false,
                displayAmount = fakeDefaultAmountFormatter,
                onCardClicked = {}
            )
        }
    }
}

private val fakeDefaultAmountFormatter: (BigDecimal) -> String = { amount ->
    val absAmount = amount.abs().setScale(2, RoundingMode.HALF_EVEN)
    when {
        amount < BigDecimal.ZERO -> {
            "-$$absAmount"
        }

        else -> {
            "$$absAmount"
        }
    }
}