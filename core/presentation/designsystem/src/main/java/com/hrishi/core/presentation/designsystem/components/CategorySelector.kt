package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.TickIcon
import com.hrishi.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.primaryFixed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CategorySelector(
    modifier: Modifier = Modifier,
    title: String? = null,
    selectedOption: T,
    fontStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    showIconBackground: Boolean = false,
    iconBackgroundColor: Color = primaryFixed,
    showMenuIcon: Boolean = true,
    options: Array<T>,
    currencyDisplay: (T) -> String,
    currencyTitleDisplay: (T) -> String,
    onItemSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            CurrencySelectorTextField(
                expanded = expanded,
                fontStyle = fontStyle,
                showIconBackground = showIconBackground,
                iconBackgroundColor = iconBackgroundColor,
                selectedOption = selectedOption,
                currencyDisplay = currencyDisplay,
                currencyTitleDisplay = currencyTitleDisplay,
                onExpandedChange = { expanded = it }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.height(48.dp), // Match height with selector
                        text = {
                            CurrencyRow(
                                fontStyle = fontStyle,
                                showIconBackground = showIconBackground,
                                iconBackgroundColor = iconBackgroundColor,
                                currencyCode = currencyDisplay(option),
                                currencyName = currencyTitleDisplay(option),
                                showMenuIcon = showMenuIcon,
                                showTickIcon = option == selectedOption
                            )
                        },
                        onClick = {
                            onItemSelected(option)
                            expanded = false
                        },
                        contentPadding = PaddingValues(
                            vertical = 0.dp,
                            horizontal = 0.dp
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> ExposedDropdownMenuBoxScope.CurrencySelectorTextField(
    expanded: Boolean,
    fontStyle: TextStyle,
    showIconBackground: Boolean,
    iconBackgroundColor: Color,
    selectedOption: T,
    currencyDisplay: (T) -> String,
    currencyTitleDisplay: (T) -> String,
    onExpandedChange: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = "",
        onValueChange = {},
        readOnly = true,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .clickable { onExpandedChange(!expanded) },
        decorationBox = @Composable {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurrencyRow(
                    showIconBackground = showIconBackground,
                    iconBackgroundColor = iconBackgroundColor,
                    currencyCode = currencyDisplay(selectedOption),
                    currencyName = currencyTitleDisplay(selectedOption),
                    fontStyle = fontStyle,
                    showMenuIcon = true,
                    modifier = Modifier.weight(1f)
                )
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
        }
    )
}

@Composable
private fun CurrencyRow(
    showIconBackground: Boolean,
    iconBackgroundColor: Color,
    currencyCode: String,
    currencyName: String,
    fontStyle: TextStyle,
    showMenuIcon: Boolean,
    showTickIcon: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (showIconBackground) {
            Spacer(modifier = Modifier.padding(start = 4.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = if (showMenuIcon) {
                            iconBackgroundColor
                        } else {
                            Color.Transparent
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(40.dp)
            ) {
                if (showMenuIcon) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = currencyCode,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Text(
                text = currencyCode,
                style = MaterialTheme.typography.labelMedium,
            )
        }

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = currencyName,
            style = fontStyle,
            textAlign = TextAlign.End
        )
        if (showTickIcon) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier.padding(end = 8.dp),
                imageVector = TickIcon,
                contentDescription = null
            )
        }
    }
}

@Composable
@Preview
fun PreviewCategorySelector() {
    SpendLessFinanceTrackerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                CategorySelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    title = "Currency",
                    selectedOption = FakeCurrency.INR,
                    options = FakeCurrency.entries.toTypedArray(),
                    currencyDisplay = { it.symbol },
                    currencyTitleDisplay = { it.title },
                    onItemSelected = {}
                )

                CategorySelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    showIconBackground = true,
                    fontStyle = MaterialTheme.typography.labelMedium,
                    selectedOption = TransactionCategoryTypeUI.HOME,
                    options = TransactionCategoryTypeUI.expenseCategories(),
                    currencyDisplay = { it.symbol },
                    currencyTitleDisplay = { it.title },
                    onItemSelected = {}
                )

                CategorySelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    showIconBackground = true,
                    iconBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    fontStyle = MaterialTheme.typography.labelMedium,
                    selectedOption = RecurringTypeUI.ONE_TIME,
                    showMenuIcon = false,
                    options = RecurringTypeUI.entries.toTypedArray(),
                    currencyDisplay = { it.symbol },
                    currencyTitleDisplay = { it.title },
                    onItemSelected = {}
                )
            }
        }
    }
}

enum class FakeCurrency(val symbol: String, val title: String) {
    INR("₹", "Indian Rupee"),
    USD("$", "US Dollar"),
    EUR("€", "Euro")
}