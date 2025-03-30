package com.hrishi.core.presentation.designsystem.components.text_field

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
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
    ),
    iconContentDescription: String? = null
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
            .onFocusChanged { isFocused = it.isFocused },
        decorationBox = { innerTextField ->
            TransactionFieldDecoration(
                value = value,
                hint = hint,
                icon = icon,
                isFocused = isFocused,
                emptyStateStyle = emptyStateStyle,
                iconContentDescription = iconContentDescription,
                innerTextField = innerTextField
            )
        }
    )
}

@Composable
private fun TransactionFieldDecoration(
    value: String,
    hint: String,
    icon: ImageVector?,
    isFocused: Boolean,
    emptyStateStyle: TextStyle,
    iconContentDescription: String?,
    innerTextField: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (value.isBlank() && !isFocused) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                icon?.let {
                    Icon(
                        tint = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.60f
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(11.dp),
                        imageVector = it,
                        contentDescription = iconContentDescription ?: hint
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

@Preview(showBackground = true)
@Composable
fun PreviewBasicTransactionField() {
    SpendLessFinanceTrackerTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var emptyValue by rememberSaveable { mutableStateOf("") }
                BasicTransactionField(
                    value = emptyValue,
                    hint = "Sender",
                    onValueChange = { emptyValue = it }
                )

                var iconValue by rememberSaveable { mutableStateOf("") }
                BasicTransactionField(
                    value = iconValue,
                    hint = "Add Note",
                    icon = PlusIcon,
                    emptyStateStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
                    ),
                    nonEmptyStateStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
                    ),
                    onValueChange = { iconValue = it }
                )
            }
        }
    }
}