package com.moneytracker.android.domain.model

import java.time.Instant

enum class AccountType { BANK, E_WALLET, CASH, SAVINGS, CREDIT, INVESTMENT, OTHER }

data class Account(
    val id: String,
    val name: String,
    val institution: String,
    val type: AccountType,
    val balance: Money,
    val lastFour: String? = null,
    val iconKey: String? = null,
    val colorArgb: Long? = null,
    val updatedAt: Instant,
    val isArchived: Boolean = false,
) {
    init {
        require(id.isNotBlank() && name.isNotBlank() && institution.isNotBlank())
        require(lastFour == null || lastFour.matches(Regex("[0-9]{4}"))) { "Only four digits are allowed" }
        require(colorArgb == null || colorArgb in 0..0xFFFFFFFFL)
    }
}
