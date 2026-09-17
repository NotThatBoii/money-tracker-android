package com.moneytracker.android.domain.model

import java.time.LocalDate

/** Informational earmarking only: saved amounts must never be added to account totals. */
data class SavingsGoal(
    val id: String,
    val name: String,
    val target: Money,
    val saved: Money,
    val targetDate: LocalDate? = null,
) {
    init {
        require(id.isNotBlank() && name.isNotBlank())
        require(target.currency == saved.currency)
        require(target.minorUnits > 0 && saved.minorUnits >= 0)
    }
}
