package com.hrishi.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.onPrimaryFixed

@Composable
fun TransactionBackground(
    modifier: Modifier = Modifier,
    hasToolbar: Boolean = true,
    gradientStartColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
    backgroundMainColor: Color = onPrimaryFixed,
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val minScreenSize = minOf(screenWidthPx, screenHeightPx)
    val gradientRadius = minScreenSize * 0.8f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundMainColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            gradientStartColor,
                            backgroundMainColor
                        ),
                        center = Offset(
                            x = screenWidthPx * 0.2f,
                            y = screenHeightPx * 0.15f
                        ),
                        radius = gradientRadius
                    ),
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (hasToolbar) Modifier else Modifier.systemBarsPadding()
                )
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewTransactionBackground() {
    TransactionBackground {

    }
}