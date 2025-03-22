package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.SpendLessWhite

@Composable
fun SpendLessErrorBanner(
    modifier: Modifier = Modifier,
    text: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.error),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                color = SpendLessWhite
            )
        )
    }
}

@Preview
@Composable
fun PreviewSpendLessErrorBanner() {
    SpendLessFinanceTrackerTheme {
        SpendLessErrorBanner(
            text = "Username or PIN is incorrect"
        )
    }
}