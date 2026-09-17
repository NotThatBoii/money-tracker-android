package com.moneytracker.android.domain

import com.moneytracker.android.domain.model.CurrencyCode
import com.moneytracker.android.domain.model.Money
import com.moneytracker.android.util.MoneyFormatter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Locale

class MoneyTest {
    @Test fun decimalConversionIsExact() {
        assertEquals(12345L, Money.fromDecimal("123.45").minorUnits)
        assertEquals(10L, Money.fromDecimal("0.10").minorUnits)
        assertEquals(-12345L, Money.fromDecimal("-123.45").minorUnits)
    }
    @Test fun excessPrecisionIsRejected() {
        assertThrows(ArithmeticException::class.java) { Money.fromDecimal("1.001") }
    }
    @Test fun ambiguousInputIsRejected() {
        listOf("1,000", "1e3", "NaN", "", " 12", ".5").forEach { value ->
            assertThrows(IllegalArgumentException::class.java) { Money.fromDecimal(value) }
        }
    }
    @Test fun valuesBeyondLongRangeAreRejected() {
        assertThrows(ArithmeticException::class.java) { Money.fromDecimal("92233720368547758.08") }
    }
    @Test fun arithmeticDoesNotOverflowSilently() {
        assertThrows(ArithmeticException::class.java) { Money(Long.MAX_VALUE) + Money(1) }
        assertThrows(ArithmeticException::class.java) { Money(Long.MIN_VALUE) - Money(1) }
    }
    @Test fun currenciesCannotBeMixed() {
        assertThrows(IllegalArgumentException::class.java) { Money(100) + Money(100, CurrencyCode("USD")) }
        assertThrows(IllegalArgumentException::class.java) { Money(100) - Money(100, CurrencyCode("USD")) }
    }
    @Test fun exactAdditionAndSubtraction() {
        assertEquals(Money(30), Money.fromDecimal("0.10") + Money.fromDecimal("0.20"))
        assertEquals(Money(-10), Money(10) - Money(20))
    }
    @Test fun phpFormattingUsesPesoAndCentavos() {
        assertEquals("₱123.45", MoneyFormatter.format(Money(12345)))
        assertEquals("₱22,960.00", MoneyFormatter.format(Money(2296000)))
        assertEquals("-₱0.01", MoneyFormatter.format(Money(-1)))
    }
    @Test fun veryLargeAmountsKeepTheirLastCentavo() {
        assertEquals("₱92,233,720,368,547,758.07", MoneyFormatter.format(Money(Long.MAX_VALUE)))
    }
    @Test fun otherCurrenciesRespectMinorUnitPrecision() {
        assertEquals(123L, Money.fromDecimal("123", CurrencyCode("JPY")).minorUnits)
        assertEquals(1234L, Money.fromDecimal("1.234", CurrencyCode("KWD")).minorUnits)
        assertEquals("$12.34", MoneyFormatter.format(Money(1234, CurrencyCode("USD")), Locale.US))
    }
    @Test fun invalidCurrencyIsRejected() {
        listOf("php", "ZZZ", "XXX", "PH").forEach { value ->
            assertThrows(IllegalArgumentException::class.java) { CurrencyCode(value) }
        }
    }
}
