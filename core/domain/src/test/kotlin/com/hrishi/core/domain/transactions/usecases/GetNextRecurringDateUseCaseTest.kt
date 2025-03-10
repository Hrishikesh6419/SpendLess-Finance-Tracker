package com.hrishi.core.domain.transactions.usecases

import com.hrishi.core.domain.model.RecurringType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month

class GetNextRecurringDateUseCaseTest {

    private val getNextRecurringDateUseCase = GetNextRecurringDateUseCase()

    @Test
    fun `Given getNextRecurringDateUseCase, When recurring type is ONE_TIME, Then return null`() {
        // Given
        val startDate = LocalDateTime.of(2025, Month.JANUARY, 15, 10, 0)

        // When
        val result = getNextRecurringDateUseCase(startDate, null, RecurringType.ONE_TIME)

        // Then
        assertEquals(null, result)
    }

    @Test
    fun `Given getNextRecurringDateUseCase, When recurring type is DAILY, Then return the next day`() {
        // Given
        val startDate = LocalDateTime.of(2025, Month.JANUARY, 15, 10, 0)

        // When
        val result = getNextRecurringDateUseCase(startDate, null, RecurringType.DAILY)

        // Then
        assertEquals(LocalDateTime.of(2025, Month.JANUARY, 16, 10, 0), result)
    }

    @Test
    fun `Given getNextRecurringDateUseCase, When recurring type is WEEKLY, Then return the next week`() {
        // Given
        val startDate = LocalDateTime.of(2025, Month.JANUARY, 15, 10, 0)

        // When
        val result = getNextRecurringDateUseCase(startDate, null, RecurringType.WEEKLY)

        // Then
        assertEquals(LocalDateTime.of(2025, Month.JANUARY, 22, 10, 0), result)
    }

    @Test
    fun `Given getNextRecurringDateUseCase, When recurring type is MONTHLY and not end of month, Then return the same day next month`() {
        // Given
        val startDate = LocalDateTime.of(2025, Month.JANUARY, 15, 10, 0)

        // When
        val result = getNextRecurringDateUseCase(startDate, null, RecurringType.MONTHLY)

        // Then
        assertEquals(LocalDateTime.of(2025, Month.FEBRUARY, 15, 10, 0), result)
    }

    @Test
    fun `Given getNextRecurringDateUseCase, When start date is last day of month and recurring type is MONTHLY, Then return last day of next month`() {
        // Given
        val startDate = LocalDateTime.of(2025, Month.JANUARY, 31, 10, 0)

        // When
        val result = getNextRecurringDateUseCase(startDate, null, RecurringType.MONTHLY)

        // Then
        assertEquals(LocalDateTime.of(2025, Month.FEBRUARY, 28, 10, 0), result)
    }

    @Test
    fun `Given getNextRecurringDateUseCase, When start date is last day of the year and recurring type is MONTHLY, Then return last day of next month`() {
        // Given
        val startDate = LocalDateTime.of(2025, Month.DECEMBER, 31, 10, 0)

        // When
        val result = getNextRecurringDateUseCase(startDate, null, RecurringType.MONTHLY)

        // Then
        assertEquals(LocalDateTime.of(2026, Month.JANUARY, 31, 10, 0), result)
    }

    @Test
    fun `Given getNextRecurringDateUseCase, When recurring type is MONTHLY and leap year, Then handle last day of month correctly`() {
        // Given
        val startDate = LocalDateTime.of(2024, Month.JANUARY, 31, 10, 0)

        // When
        val result = getNextRecurringDateUseCase(startDate, null, RecurringType.MONTHLY)

        // Then
        assertEquals(LocalDateTime.of(2024, Month.FEBRUARY, 29, 10, 0), result)
    }

    @Test
    fun `Given getNextRecurringDateUseCase, When last transaction date exists for MONTHLY, Then use lastTransactionDate to calculate next date`() {
        // Given
        val startDate = LocalDateTime.of(2025, Month.JANUARY, 15, 10, 0)
        val lastTransactionDate = LocalDateTime.of(2025, Month.FEBRUARY, 15, 10, 0)

        // When
        val result =
            getNextRecurringDateUseCase(startDate, lastTransactionDate, RecurringType.MONTHLY)

        // Then
        assertEquals(LocalDateTime.of(2025, Month.MARCH, 15, 10, 0), result)
    }

    @Test
    fun `Given getNextRecurringDateUseCase, When last transaction date is end of month for MONTHLY, Then return last day of next month`() {
        // Given
        val startDate = LocalDateTime.of(2025, Month.JANUARY, 31, 10, 0)
        val lastTransactionDate = LocalDateTime.of(2025, Month.FEBRUARY, 28, 10, 0)

        // When
        val result =
            getNextRecurringDateUseCase(startDate, lastTransactionDate, RecurringType.MONTHLY)

        // Then
        assertEquals(LocalDateTime.of(2025, Month.MARCH, 31, 10, 0), result)
    }

    @Test
    fun `Given getNextRecurringDateUseCase, When recurring type is YEARLY, Then return the next year`() {
        // Given
        val startDate = LocalDateTime.of(2025, Month.JANUARY, 15, 10, 0)

        // When
        val result = getNextRecurringDateUseCase(startDate, null, RecurringType.YEARLY)

        // Then
        assertEquals(LocalDateTime.of(2026, Month.JANUARY, 15, 10, 0), result)
    }
}
