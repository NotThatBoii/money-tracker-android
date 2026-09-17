package com.moneytracker.android.util

import com.moneytracker.android.domain.model.Money
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object MoneyFormatter {
    /** New formatter per call: NumberFormat is mutable and not thread safe. */
    fun format(money: Money, locale: Locale = Locale.forLanguageTag("en-PH")): String =
        NumberFormat.getCurrencyInstance(locale).apply {
            currency = Currency.getInstance(money.currency.value)
            minimumFractionDigits = money.currency.fractionDigits
            maximumFractionDigits = money.currency.fractionDigits
        }.format(money.toDecimal())
}
