package com.hrishi.core.presentation.designsystem.components.text_field

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.PlusIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme

@Composable
fun BasicTransactionField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    icon: ImageVector? = null,
    onValueChange: (String) -> Unit,
    emptyStateStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
    ),
    nonEmptyStateStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    )
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }

    val textStyle by remember(value, isFocused) {
        derivedStateOf {
            if (value.isBlank() && !isFocused) emptyStateStyle
            else nonEmptyStateStyle
        }
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = textStyle.copy(
            textAlign = TextAlign.Center
        ),
        modifier = modifier
            .fillMaxWidth()
            .focusable()
            .onFocusChanged { isFocused = it.isFocused },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (value.isBlank() && !isFocused) {
                        icon?.let {
                            Icon(
                                tint = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.60f
                                ),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(11.dp),
                                imageVector = it,
                                contentDescription = ""
                            )
                        }
                        Text(
                            text = hint,
                            style = emptyStateStyle.copy(
                                textAlign = TextAlign.Center
                            ),
                        )
                    }
                }
                innerTextField()
            }
        }
    )
}


@Preview
@Composable
fun PreviewBasicTransactionField() {
    SpendLessFinanceTrackerTheme {
        var value by rememberSaveable { mutableStateOf("") }
        var value1 by rememberSaveable { mutableStateOf("") }
        var value2 by rememberSaveable { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicTransactionField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    hint = "Sender",
                    onValueChange = { value = it }
                )

                BasicTransactionField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value1,
                    hint = "Sender",
                    onValueChange = { value1 = it }
                )

                BasicTransactionField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value2,
                    hint = "Add Note",
                    icon = PlusIcon,
                    emptyStateStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
                    ),
                    nonEmptyStateStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
                    ),
                    onValueChange = { value2 = it }
                )
            }
        }
    }
}