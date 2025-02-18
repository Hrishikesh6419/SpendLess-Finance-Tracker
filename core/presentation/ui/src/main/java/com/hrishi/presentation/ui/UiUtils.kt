package com.hrishi.presentation.ui

const val MAX_PIN_LENGTH = 5

fun Long.formatToTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}