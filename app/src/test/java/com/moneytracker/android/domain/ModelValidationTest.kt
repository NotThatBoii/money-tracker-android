package com.moneytracker.android.domain

import com.moneytracker.android.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.Instant
import java.time.YearMonth

class ModelValidationTest {
    private fun transaction(type: TransactionType, destination: String? = null, category: String? = "food", amount: Long = 100) =
        FinancialTransaction("t", Money(amount), type, "a", destination, category, "Test", occurredAt = Instant.EPOCH, createdAt = Instant.EPOCH)

    @Test fun transferRequiresDifferentDestinationAndNoSpendingCategory() {
        assertThrows(IllegalArgumentException::class.java) { transaction(TransactionType.TRANSFER) }
        assertThrows(IllegalArgumentException::class.java) { transaction(TransactionType.TRANSFER, "a", null) }
        assertThrows(IllegalArgumentException::class.java) { transaction(TransactionType.TRANSFER, "b", "food") }
        assertEquals("b", transaction(TransactionType.TRANSFER, "b", null).destinationAccountId)
    }
    @Test fun incomeAndExpenseRequireCategoryAndNoDestination() {
        listOf(TransactionType.INCOME, TransactionType.EXPENSE).forEach { type ->
            assertThrows(IllegalArgumentException::class.java) { transaction(type, "b") }
            assertThrows(IllegalArgumentException::class.java) { transaction(type, category = null) }
            assertThrows(IllegalArgumentException::class.java) { transaction(type, amount = 0) }
            assertThrows(IllegalArgumentException::class.java) { transaction(type, amount = -1) }
        }
    }
    @Test fun onlyLastFourDigitsMayBeStored() {
        assertThrows(IllegalArgumentException::class.java) {
            Account("a", "BPI", "BPI", AccountType.BANK, Money(0), lastFour = "1234567890123456", updatedAt = Instant.EPOCH)
        }
    }
    @Test fun budgetsMustBePositive() {
        assertThrows(IllegalArgumentException::class.java) { Budget("b", "food", YearMonth.of(2026, 9), Money(0)) }
    }
    @Test fun goalsRequireMatchingCurrencyAndNonnegativeSavings() {
        assertThrows(IllegalArgumentException::class.java) { SavingsGoal("g", "Laptop", Money(100), Money(-1)) }
        assertThrows(IllegalArgumentException::class.java) { SavingsGoal("g", "Laptop", Money(100), Money(1, CurrencyCode("USD"))) }
    }
    @Test fun defaultCategoriesHaveUniqueStableIdentifiers() {
        assertEquals(15, DefaultCategories.all.size)
        assertEquals(15, DefaultCategories.all.map { it.id }.distinct().size)
    }
}
