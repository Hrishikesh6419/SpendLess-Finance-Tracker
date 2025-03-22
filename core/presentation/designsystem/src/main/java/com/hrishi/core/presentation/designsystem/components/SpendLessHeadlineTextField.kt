package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun SpendLessHeadlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onDone: (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = if (enabled) 0.08f else 0.04f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            maxLines = 1,
            textStyle = MaterialTheme.typography.displayMedium.copy(
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone?.invoke()
                }
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerBox ->
                HeadlineTextFieldContent(
                    value = value,
                    hint = hint,
                    isFocused = isFocused,
                    enabled = enabled,
                    innerTextField = innerBox
                )
            }
        )
    }
}

@Composable
private fun HeadlineTextFieldContent(
    value: String,
    hint: String,
    isFocused: Boolean,
    enabled: Boolean,
    innerTextField: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (value.isEmpty() && !isFocused) {
            Text(
                text = hint.lowercase(Locale.getDefault()),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = if (enabled) 0.38f else 0.25f
                ),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            innerTextField()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpendLessHeadlineTextField() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SpendLessHeadlineTextField(
            value = "",
            onValueChange = {},
            hint = "Username",
            modifier = Modifier.fillMaxWidth(),
        )

        SpendLessHeadlineTextField(
            value = "James Bond",
            onValueChange = {},
            hint = "Username",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}