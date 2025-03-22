package com.hrishi.core.presentation.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
    iconUnSelectedColor: Color = onPrimaryFixed.copy(alpha = 0.70f),
    textSelectedColor: Color = MaterialTheme.colorScheme.onSurface,
    textUnSelectedColor: Color = onPrimaryFixed.copy(alpha = 0.70f),
    enabled: Boolean = true
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
                .background(primaryFixed)
                .semantics { contentDescription = title ?: "Segmented selector" },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { option ->
                SegmentedSelectorOption(
                    option = option,
                    isSelected = selectedOption == option,
                    onOptionSelected = { onOptionSelected(option) },
                    displayText = displayText,
                    displayIcon = displayIcon,
                    iconSelectedColor = iconSelectedColor,
                    iconUnSelectedColor = iconUnSelectedColor,
                    textSelectedColor = textSelectedColor,
                    textUnSelectedColor = textUnSelectedColor,
                    enabled = enabled
                )
            }
        }
    }
}

@Composable
private fun <T> RowScope.SegmentedSelectorOption(
    option: T,
    isSelected: Boolean,
    onOptionSelected: () -> Unit,
    displayText: (T) -> String,
    displayIcon: ((T) -> Int)?,
    iconSelectedColor: Color,
    iconUnSelectedColor: Color,
    textSelectedColor: Color,
    textUnSelectedColor: Color,
    enabled: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val optionText = displayText(option)

    Box(
        modifier = Modifier
            .weight(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.surfaceContainerLowest
                } else {
                    Color.Transparent
                }
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                role = Role.Tab,
                onClick = onOptionSelected
            )
            .padding(vertical = 8.dp)
            .semantics {
                selected = isSelected
                stateDescription = if (isSelected) "Selected" else "Not selected"
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            displayIcon?.let { iconProvider ->
                val iconRes = iconProvider(option)
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    tint = if (isSelected) {
                        iconSelectedColor
                    } else {
                        iconUnSelectedColor
                    }.let { if (enabled) it else it.copy(alpha = 0.38f) },
                    painter = painterResource(id = iconRes),
                    contentDescription = null
                )
            }

            Text(
                text = optionText,
                color = if (isSelected) {
                    textSelectedColor
                } else {
                    textUnSelectedColor
                }.let { if (enabled) it else it.copy(alpha = 0.38f) },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedSelectorPreview() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                SegmentedSelector(
                    title = "Thousands separator",
                    options = FakeThousandsSeparator.entries.toTypedArray(),
                    selectedOption = FakeThousandsSeparator.DOT,
                    onOptionSelected = {},
                    displayText = { it.displayText() }
                )

                SegmentedSelector(
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

private enum class FakeThousandsSeparator {
    DOT, COMMA, SPACE;

    fun displayText(): String {
        return when (this) {
            DOT -> "1.000"
            COMMA -> "1,000"
            SPACE -> "1 000"
        }
    }
}

private enum class TransactionType(@DrawableRes val iconRes: Int) {
    EXPENSE(R.drawable.ic_expense),
    INCOME(R.drawable.ic_income);

    fun displayText(): String {
        return when (this) {
            EXPENSE -> "Expense"
            INCOME -> "Income"
        }
    }
}