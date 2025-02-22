package com.hrishi.core.presentation.designsystem.components.buttons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.ArrowForward
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme

@Composable
fun SpendLessButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    enabledTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    disabledTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    enabledContainerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.onSurface.copy(
        alpha = 0.12f
    ),
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(
        alpha = 0.38f
    ),
    isEnabled: Boolean = true,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonColors(
            containerColor = enabledContainerColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledContainerColor
        )
    ) {
        Text(
            text = buttonText,
            style = if (isEnabled) {
                enabledTextStyle
            } else {
                disabledTextStyle
            },
            color = if (isEnabled) {
                contentColor
            } else {
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.38f
                )
            }
        )
        Spacer(modifier = Modifier.width(8.dp))

        icon?.let {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 2.dp),
                imageVector = it,
                contentDescription = ""
            )
        }
    }
}

@Preview
@Composable
fun PreviewSpendLessButton() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SpendLessButton(
                buttonText = "Next",
                isEnabled = true,
                icon = ArrowForward
            ) {

            }
        }
    }
}