package com.moneytracker.android.domain

import com.moneytracker.android.domain.model.Account
import com.moneytracker.android.domain.model.AccountType
import com.moneytracker.android.domain.model.CurrencyCode
import com.moneytracker.android.domain.model.Money
import com.moneytracker.android.domain.usecase.CalculateAccountTotals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class AccountTotalsTest {
    private val calculate = CalculateAccountTotals()
    private fun account(id: String, minor: Long, archived: Boolean = false, currency: CurrencyCode = CurrencyCode.PHP) =
        Account(id, id, "Custom", AccountType.BANK, Money(minor, currency), updatedAt = Instant.EPOCH, isArchived = archived)

    @Test fun sampleAccountTotalIs22960Pesos() {
        val values = listOf(account("BPI", 1245000), account("GCash", 321000), account("Maya", 580000), account("Cash", 150000))
        assertEquals(Money(2296000), calculate(values)[CurrencyCode.PHP])
    }
    @Test fun archivedAccountsAreExcluded() {
        assertEquals(Money(100), calculate(listOf(account("a", 100), account("b", 500, archived = true)))[CurrencyCode.PHP])
    }
    @Test fun signedLiabilitiesReduceTotal() {
        assertEquals(Money(75), calculate(listOf(account("a", 100), account("credit", -25)))[CurrencyCode.PHP])
    }
    @Test fun currenciesRemainSeparate() {
        val totals = calculate(listOf(account("a", 100), account("b", 200, currency = CurrencyCode("USD"))))
        assertEquals(2, totals.size)
        assertEquals(Money(100), totals[CurrencyCode.PHP])
    }
    @Test fun emptyAccountsHaveNoCurrencyTotals() { assertTrue(calculate(emptyList()).isEmpty()) }
    @Test fun totalOverflowIsRejected() {
        assertThrows(ArithmeticException::class.java) { calculate(listOf(account("a", Long.MAX_VALUE), account("b", 1))) }
    }
}
