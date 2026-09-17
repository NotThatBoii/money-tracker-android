package com.moneytracker.android.domain.model

import java.time.Instant

enum class TransactionType { EXPENSE, INCOME, TRANSFER }

data class FinancialTransaction(
    val id: String,
    val amount: Money,
    val type: TransactionType,
    val accountId: String,
    val destinationAccountId: String? = null,
    val categoryId: String? = null,
    val description: String,
    val notes: String? = null,
    val occurredAt: Instant,
    val createdAt: Instant,
) {
    init {
        require(id.isNotBlank() && accountId.isNotBlank())
        require(amount.minorUnits > 0) { "Transaction amounts must be positive" }
        if (type == TransactionType.TRANSFER) {
            require(!destinationAccountId.isNullOrBlank() && destinationAccountId != accountId)
            require(categoryId == null) { "Transfers are not category spending" }
        } else {
            require(destinationAccountId == null)
            require(!categoryId.isNullOrBlank())
        }
    }
}
