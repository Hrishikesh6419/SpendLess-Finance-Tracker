package com.hrishi.domain.model

enum class Currency(val symbol: String, val title: String) {
    USD("$", "US Dollar (USD)"),
    EURO("€", "Euro (EUR)"),
    POUND("£", "British Pound Sterling (GBP)"),
    YEN("¥", "Japanese Yen (JPY)"),
    SWISS("CHF", "Swiss Franc (CHF)"),
    CAD("C$", "Canadian Dollar (CAD)"),
    AUD("A$", "Australian Dollar (AUD)"),
    CNY("¥", "Chinese Yuan Renminbi (CNY)"),
    INR("₹", "Indian Rupee (INR)"),
    ZAR("R", "South African Rand (ZAR)")
}