package com.hrishi.core.presentation.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hrishi.presentation.designsystem.R

val Figtree = FontFamily(
    Font(
        resId = R.font.figtree_variablefont_weight,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.figtree_variablefont_weight,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.figtree_variablefont_weight,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.figtree_variablefont_weight,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.figtree_variablefont_weight,
        weight = FontWeight.Bold
    )
)

val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        color = SpendLessDarkGrey
    ),
    bodyMedium = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        color = SpendLessBlack
    ),
    headlineLarge = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        color = SpendLessBlack
    )
)