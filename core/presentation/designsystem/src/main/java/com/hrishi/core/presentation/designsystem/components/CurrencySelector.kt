package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.TickIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CurrencySelector(
    modifier: Modifier = Modifier,
    title: String? = null,
    selectedOption: T,
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
                selectedOption = selectedOption,
                currencyDisplay = currencyDisplay,
                currencyTitleDisplay = currencyTitleDisplay,
                onExpandedChange = { expanded = it }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceContainerLowest
                )
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            CurrencyRow(
                                currencyCode = currencyDisplay(option),
                                currencyName = currencyTitleDisplay(option),
                                showTickIcon = option == selectedOption
                            )
                        },
                        onClick = {
                            onItemSelected(option)
                            expanded = false
                        }
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
    selectedOption: T,
    currencyDisplay: (T) -> String,
    currencyTitleDisplay: (T) -> String,
    onExpandedChange: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    TextField(
        value = "",
        onValueChange = {},
        readOnly = true,
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .clickable { onExpandedChange(!expanded) },
        singleLine = true,
        interactionSource = interactionSource,
        placeholder = {
            CurrencyRow(
                currencyCode = currencyDisplay(selectedOption),
                currencyName = currencyTitleDisplay(selectedOption)
            )
        }
    )
}

@Composable
private fun CurrencyRow(
    currencyCode: String,
    currencyName: String,
    showTickIcon: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currencyCode,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = currencyName,
            style = MaterialTheme.typography.bodyMedium,
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
fun PreviewCurrencySelector() {
    SpendLessFinanceTrackerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CurrencySelector(
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
        }
    }
}

enum class FakeCurrency(val symbol: String, val title: String) {
    INR("₹", "Indian Rupee"),
    USD("$", "US Dollar"),
    EUR("€", "Euro")
}