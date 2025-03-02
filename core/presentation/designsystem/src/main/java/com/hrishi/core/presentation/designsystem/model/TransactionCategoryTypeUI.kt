package com.hrishi.core.presentation.designsystem.model

enum class TransactionCategoryTypeUI(val symbol: String, val title: String) {
    HOME("\uD83C\uDFE0", "Home"),
    FOOD("\uD83C\uDF55", "Food & Groceries"),
    ENTERTAINMENT("\uD83D\uDCBB", "Entertainment"),
    CLOTHING("\uD83D\uDC54", "Clothing & Accessories"),
    HEALTH("❤\uFE0F", "Health & Wellness"),
    PERSONAL_CARE("\uD83D\uDEC1", "Personal Care"),
    TRANSPORTATION("\uD83D\uDE97", "Transportation"),
    EDUCATION("\uD83C\uDF93", "Education"),
    SAVINGS("\uD83D\uDC8E", "Saving & Investments"),
    OTHER("⚙\uFE0F", "Other"),
    INCOME("\uD83D\uDCB0", "Income");

    private fun isIncome(): Boolean = this == INCOME

    fun isExpense(): Boolean = !isIncome()

    companion object {
        fun incomeCategories(): Array<TransactionCategoryTypeUI> = arrayOf(INCOME)

        fun expenseCategories(): Array<TransactionCategoryTypeUI> = entries.filter { it.isExpense() }.toTypedArray()
    }
}