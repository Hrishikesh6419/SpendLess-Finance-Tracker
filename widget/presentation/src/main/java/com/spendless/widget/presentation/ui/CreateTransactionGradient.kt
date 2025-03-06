package com.spendless.widget.presentation.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ColumnScope
import androidx.glance.layout.fillMaxSize
import com.hrishi.core.presentation.designsystem.onPrimaryFixed

@Composable
fun CreateTransactionGradient(
    modifier: GlanceModifier = GlanceModifier,
    colorsList: List<Color> = listOf(
        onPrimaryFixed,
        onPrimaryFixed,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primary,
        Color(0xFFAF40FE)
    ),
    cornerRadius: Float = 0f,
    content: @Composable ColumnScope.() -> Unit
) {
    val gradientBitmap = remember {
        createGradientBitmap(
            colors = colorsList,
            cornerRadius = cornerRadius
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ImageProvider(gradientBitmap))
    ) {
        Column {
            content()
        }
    }
}

private fun createGradientBitmap(
    colors: List<Color>,
    width: Int = 500,
    height: Int = 500,
    cornerRadius: Float = 0f
): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        shader = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            colors.map { it.toArgb() }.toIntArray(),
            null,
            Shader.TileMode.CLAMP
        )
        isAntiAlias = true
    }

    if (cornerRadius > 0) {
        canvas.drawRoundRect(
            0f, 0f, width.toFloat(), height.toFloat(),
            cornerRadius, cornerRadius, paint
        )
    } else {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    return bitmap
}