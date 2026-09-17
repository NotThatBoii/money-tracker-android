package com.moneytracker.android.domain.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

/** ISO currency with its minor-unit precision. No implicit foreign-exchange conversion. */
@JvmInline
value class CurrencyCode(val value: String) {
    init {
        require(value.matches(Regex("[A-Z]{3}"))) { "Use an uppercase ISO currency code" }
        require(Currency.getInstance(value).defaultFractionDigits in 0..4) { "Unsupported currency" }
    }
    val fractionDigits: Int get() = Currency.getInstance(value).defaultFractionDigits
    companion object { val PHP = CurrencyCode("PHP") }
}

/** Signed integer minor units; checked arithmetic fails instead of silently overflowing. */
data class Money(val minorUnits: Long, val currency: CurrencyCode = CurrencyCode.PHP) {
    operator fun plus(other: Money): Money {
        require(currency == other.currency) { "Cannot combine different currencies" }
        return copy(minorUnits = Math.addExact(minorUnits, other.minorUnits))
    }
    operator fun minus(other: Money): Money {
        require(currency == other.currency) { "Cannot combine different currencies" }
        return copy(minorUnits = Math.subtractExact(minorUnits, other.minorUnits))
    }
    fun toDecimal(): BigDecimal = BigDecimal.valueOf(minorUnits, currency.fractionDigits)

    companion object {
        /** Strict decimal input; reject excess precision instead of rounding a user's money. */
        fun fromDecimal(value: String, currency: CurrencyCode = CurrencyCode.PHP): Money {
            require(value.matches(Regex("-?[0-9]+(\\.[0-9]+)?"))) { "Enter a plain decimal amount" }
            val amount = BigDecimal(value).setScale(currency.fractionDigits, RoundingMode.UNNECESSARY)
            return Money(amount.movePointRight(currency.fractionDigits).longValueExact(), currency)
        }
    }
}
