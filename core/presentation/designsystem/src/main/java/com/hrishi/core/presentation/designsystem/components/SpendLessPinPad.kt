package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.BackDelete
import com.hrishi.core.presentation.designsystem.FingerPrint
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme

@Composable
fun SpendLessPinPad(
    modifier: Modifier = Modifier,
    hasBiometricButton: Boolean = false,
    onBiometricButtonClicked: (() -> Unit)? = null,
    onNumberPressedClicked: (Int) -> Unit,
    onDeletePressedClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until 3) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (col in 0 until 3) {
                    val number = row * 3 + col + 1
                    PinPadButton(
                        text = number.toString(),
                        onClick = { onNumberPressedClicked(number) }
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (hasBiometricButton) {
                PinPadButton(
                    icon = FingerPrint,
                    onClick = onBiometricButtonClicked,
                    alpha = 0.30f
                )
            } else {
                Spacer(modifier = Modifier.size(108.dp)) // Empty space if biometric is disabled
            }

            PinPadButton(
                text = "0",
                onClick = { onNumberPressedClicked(0) }
            )

            PinPadButton(
                icon = BackDelete,
                onClick = onDeletePressedClicked,
                alpha = 0.30f
            )
        }
    }
}

@Composable
private fun PinPadButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    alpha: Float = 1f
) {
    Box(
        modifier = modifier
            .size(108.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha))
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        text?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }

        icon?.let {
            Image(
                imageVector = it,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewSpendLessPinPad() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SpendLessPinPad(
                modifier = Modifier.padding(16.dp),
                hasBiometricButton = true,
                onNumberPressedClicked = {

                },
                onDeletePressedClicked = {

                }
            )
        }
    }
}