package com.moneytracker.android.domain.usecase

import com.moneytracker.android.domain.model.Account
import com.moneytracker.android.domain.model.CurrencyCode
import com.moneytracker.android.domain.model.Money

/** Return one total per currency until explicit exchange-rate support is implemented. */
class CalculateAccountTotals {
    operator fun invoke(accounts: List<Account>): Map<CurrencyCode, Money> = accounts
        .filterNot { it.isArchived }
        .groupBy { it.balance.currency }
        .mapValues { (currency, group) ->
            group.fold(Money(0, currency)) { total, account -> total + account.balance }
        }
}
