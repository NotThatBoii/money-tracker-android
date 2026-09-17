package com.moneytracker.android.domain.model

import java.time.YearMonth

data class Budget(val id: String, val categoryId: String, val month: YearMonth, val limit: Money) {
    init {
        require(id.isNotBlank() && categoryId.isNotBlank())
        require(limit.minorUnits > 0)
    }
}
