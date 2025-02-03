package com.hrishi.core.presentation.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.unit.sp
import com.hrishi.presentation.designsystem.R

@OptIn(ExperimentalTextApi::class)
val FigtreeRegular = FontFamily(
    Font(
        R.font.figtree_variablefont_weight,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    )
)

@OptIn(ExperimentalTextApi::class)
val FigtreeMedium = FontFamily(
    Font(
        R.font.figtree_variablefont_weight,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(500)
        )
    )
)

@OptIn(ExperimentalTextApi::class)
val FigtreeSemiBold = FontFamily(
    Font(
        R.font.figtree_variablefont_weight,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(600)
        )
    )
)

val Typography = Typography(
    // 🎨 Display
    displayLarge = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),

    // 📰 Headline
    headlineLarge = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),

    // 🔠 Title
    titleLarge = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FigtreeMedium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    // 🔖 Label
    labelMedium = TextStyle(
        fontFamily = FigtreeMedium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FigtreeMedium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    // 📖 Body
    bodyMedium = TextStyle(
        fontFamily = FigtreeRegular,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FigtreeRegular,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = SpendLessDarkGrey
    )
)