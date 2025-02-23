package com.hrishi.core.presentation.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.onPrimaryFixed
import com.hrishi.core.presentation.designsystem.primaryFixed
import com.hrishi.presentation.designsystem.R

@Composable
fun <T> SegmentedSelector(
    modifier: Modifier = Modifier,
    title: String? = null,
    options: Array<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    displayText: (T) -> String,
    displayIcon: ((T) -> Int)? = null,
    iconSelectedColor: Color = MaterialTheme.colorScheme.primary,
    iconUnSelectedColor: Color = onPrimaryFixed.copy(
        alpha = 0.70f
    ),
    textSelectedColor: Color = MaterialTheme.colorScheme.onSurface,
    textUnSelectedColor: Color = onPrimaryFixed.copy(
        alpha = 0.70f
    )
) {
    Column(modifier = modifier) {
        title?.let {
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = it,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(primaryFixed),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { option ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (selectedOption == option) {
                                MaterialTheme.colorScheme.surfaceContainerLowest
                            } else {
                                Color.Transparent
                            }
                        )
                        .clickable { onOptionSelected(option) }
                        .indication(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current
                        )
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        displayIcon?.let {
                            Icon(
                                modifier = Modifier.padding(end = 8.dp),
                                tint = if (selectedOption == option) {
                                    iconSelectedColor
                                } else {
                                    iconUnSelectedColor
                                },
                                painter = painterResource(id = displayIcon(option)),
                                contentDescription = ""
                            )
                        }
                        Text(
                            text = displayText(option),
                            color = if (selectedOption == option) {
                                textSelectedColor
                            } else {
                                textUnSelectedColor
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSegmentedSelector() {
    SpendLessFinanceTrackerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                SegmentedSelector(
                    modifier = Modifier.padding(16.dp),
                    title = "Thousands separator",
                    options = FakeThousandsSeparator.entries.toTypedArray(),
                    selectedOption = FakeThousandsSeparator.DOT,
                    onOptionSelected = {},
                    displayText = { it.displayText() }
                )

                SegmentedSelector(
                    modifier = Modifier.padding(16.dp),
                    options = TransactionType.entries.toTypedArray(),
                    selectedOption = TransactionType.EXPENSE,
                    onOptionSelected = {},
                    displayText = { it.displayText() },
                    displayIcon = { it.iconRes },
                    textSelectedColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Fake Enum for UI Previews (Since `designSystem` can't access `domain`)
enum class FakeThousandsSeparator {
    DOT, COMMA, SPACE;

    fun displayText(): String {
        return when (this) {
            DOT -> "1.000"
            COMMA -> "1,000"
            SPACE -> "1 000"
        }
    }
}

enum class TransactionType(@DrawableRes val iconRes: Int) {
    EXPENSE(R.drawable.ic_expense),
    INCOME(R.drawable.ic_income);

    fun displayText(): String {
        return when (this) {
            EXPENSE -> "Expense"
            INCOME -> "Income"
        }
    }
}