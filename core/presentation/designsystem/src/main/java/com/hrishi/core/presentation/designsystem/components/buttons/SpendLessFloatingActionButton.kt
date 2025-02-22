package com.hrishi.core.presentation.designsystem.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.PlusIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme

@Composable
fun SpendLessFloatingActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = PlusIcon,
    iconColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    contentDescription: String = "",
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(60.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier,
            tint = iconColor,
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@Preview
@Composable
fun PreviewSpendLessFloatingActionButton() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SpendLessFloatingActionButton(
                contentDescription = "Add new transaction"
            ) {
            }
        }
    }
}