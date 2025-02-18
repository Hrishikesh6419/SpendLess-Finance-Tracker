package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.BackArrow
import com.hrishi.core.presentation.designsystem.ExitIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme

@Composable
fun SpendLessTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    startIcon: ImageVector? = BackArrow,
    onStartIconClick: (() -> Unit)? = null,
    endIcon: ImageVector? = null,
    endIconColor: Color = MaterialTheme.colorScheme.error,
    onEndIconClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        startIcon?.let {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onStartIconClick?.invoke()
                    },
                imageVector = it,
                contentDescription = ""
            )
        }

        title?.let {
            Text(
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 16.dp
                ),
                text = it
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        endIcon?.let {
            Icon(
                tint = endIconColor,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        onEndIconClick?.invoke()
                    },
                imageVector = it,
                contentDescription = ""
            )
        }
    }
}

@Preview
@Composable
fun PreviewSpendLessTopBar() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SpendLessTopBar(
                endIcon = ExitIcon,
                title = "Hi",
                onStartIconClick = {

                },
                onEndIconClick = {

                }
            )
        }
    }
}