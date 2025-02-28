package com.hrishi.core.presentation.designsystem.components.text_field

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme

@Composable
fun BasicTransactionField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
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
                if (value.isBlank() && !isFocused) {
                    Text(
                        text = hint,
                        style = emptyStateStyle.copy(
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
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
            }
        }
    }
}