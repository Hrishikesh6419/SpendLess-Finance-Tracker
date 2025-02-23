package com.hrishi.core.presentation.designsystem.components.text_field

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            if (value.isBlank() && !isFocused) emptyStateStyle else nonEmptyStateStyle
        }
    }

    BasicTextField(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .widthIn(min = 100.dp),
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .focusable()
                    .onFocusChanged { isFocused = it.isFocused }
            ) {
                if (value.isBlank() && !isFocused) {
                    Text(
                        text = hint,
                        style = emptyStateStyle
                    )
                }
                innerTextField()
            }
        },
        singleLine = true
    )
}

@Preview
@Composable
fun PreviewBasicTransactionField() {
    SpendLessFinanceTrackerTheme {
        var value by rememberSaveable { mutableStateOf("") }
        var value1 by rememberSaveable { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    BasicTransactionField(
                        value = value,
                        hint = "Sender",
                        onValueChange = { value = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    BasicTransactionField(
                        value = value1,
                        hint = "Sender",
                        onValueChange = { value1 = it }
                    )
                }
            }
        }
    }
}