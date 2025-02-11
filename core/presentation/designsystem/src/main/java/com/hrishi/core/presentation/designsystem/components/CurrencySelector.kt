package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.runtime.rememberUpdatedState
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
    title: String,
    selectedOption: T,
    options: Array<T>,
    displayContent1: (T) -> String,
    displayContent2: (T) -> String,
    displayMenuItem1: (T) -> String,
    displayMenuItem2: (T) -> String,
    onItemSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Remember updated states for functions to prevent recompositions
    val currentDisplayContent1 by rememberUpdatedState(newValue = displayContent1)
    val currentDisplayContent2 by rememberUpdatedState(newValue = displayContent2)
    val currentDisplayMenuItem1 by rememberUpdatedState(newValue = displayMenuItem1)
    val currentDisplayMenuItem2 by rememberUpdatedState(newValue = displayMenuItem2)

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall
        )

        ExposedDropdownMenuBox(
            modifier = Modifier.padding(top = 6.dp),
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
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
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .shadow(2.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .clickable { expanded = !expanded },
                singleLine = true,
                interactionSource = remember { MutableInteractionSource() },
                placeholder = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentDisplayContent1(selectedOption),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentDisplayContent2(selectedOption),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )

            ExposedDropdownMenu(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentDisplayMenuItem1(option),
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentDisplayMenuItem2(option),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.End
                                )
                                if (option == selectedOption) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Image(
                                        modifier = Modifier.padding(end = 8.dp),
                                        imageVector = TickIcon,
                                        contentDescription = null
                                    )
                                }
                            }
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
                displayContent1 = {
                    it.symbol
                },
                displayContent2 = {
                    it.title
                },
                displayMenuItem1 = {
                    it.symbol
                },
                displayMenuItem2 = {
                    it.title
                },
                onItemSelected = {

                }
            )
        }
    }
}

enum class FakeCurrency(val symbol: String, val title: String) {
    USD("$", "US Dollar (USD)"),
    INR("₹", "Rupee")
}