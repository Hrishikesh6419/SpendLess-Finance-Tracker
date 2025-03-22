package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.secondaryFixed

@Composable
fun PreviousWeekTotalView(
    modifier: Modifier = Modifier,
    amount: String,
    description: String,
    backgroundColor: Color = secondaryFixed,
    contentDescription: String? = null
) {
    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 72.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        AmountDisplay(amount)
        DescriptionDisplay(description)
    }
}

@Composable
private fun AmountDisplay(amount: String) {
    Text(
        text = amount,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun DescriptionDisplay(description: String) {
    Text(
        modifier = Modifier.padding(top = 2.dp),
        text = description,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPreviousWeekTotalView() {
    SpendLessFinanceTrackerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PreviousWeekTotalView(
                    amount = "-$59.99",
                    description = "Previous week",
                )

                PreviousWeekTotalView(
                    amount = "$1,247.35",
                    description = "Previous month",
                    backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                )
            }
        }
    }
}