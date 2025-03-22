package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.hrishi.core.presentation.designsystem.primaryFixed

@Composable
fun PopularCategoryView(
    modifier: Modifier = Modifier,
    icon: String,
    background: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.10f),
    iconBackground: Color = primaryFixed,
    title: String,
    description: String,
    contentDescription: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = background,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            }
    ) {
        CategoryIcon(
            icon = icon,
            iconBackground = iconBackground
        )

        Spacer(modifier = Modifier.width(8.dp))

        CategoryInfo(
            title = title,
            description = description
        )
    }
}

@Composable
private fun CategoryIcon(
    icon: String,
    iconBackground: Color
) {
    Box(
        modifier = Modifier
            .background(
                color = iconBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 30.sp
        )
    }
}

@Composable
private fun RowScope.CategoryInfo(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier.align(Alignment.CenterVertically)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPopularCategoryView() {
    SpendLessFinanceTrackerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryGradientBackground {
                PopularCategoryView(
                    modifier = Modifier.padding(16.dp),
                    icon = "🍕",
                    title = "Food & Groceries",
                    description = "Most popular category",
                    contentDescription = "Food and Groceries is your most popular spending category"
                )
            }
        }
    }
}