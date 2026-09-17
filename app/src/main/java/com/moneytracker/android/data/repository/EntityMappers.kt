package com.moneytracker.android.data.repository

import com.moneytracker.android.data.local.entity.AccountEntity
import com.moneytracker.android.data.local.entity.BudgetEntity
import com.moneytracker.android.data.local.entity.CategoryEntity
import com.moneytracker.android.data.local.entity.SavingsGoalEntity
import com.moneytracker.android.data.local.entity.TransactionEntity
import com.moneytracker.android.domain.model.Account
import com.moneytracker.android.domain.model.AccountType
import com.moneytracker.android.domain.model.Budget
import com.moneytracker.android.domain.model.Category
import com.moneytracker.android.domain.model.CurrencyCode
import com.moneytracker.android.domain.model.FinancialTransaction
import com.moneytracker.android.domain.model.Money
import com.moneytracker.android.domain.model.SavingsGoal
import com.moneytracker.android.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth

internal fun AccountEntity.toDomain() = Account(
    id, name, institution, AccountType.valueOf(type), Money(balanceMinor, CurrencyCode(currencyCode)),
    lastFour, iconKey, colorArgb, Instant.ofEpochMilli(updatedAtEpochMillis), isArchived,
)
internal fun TransactionEntity.toDomain() = FinancialTransaction(
    id, Money(amountMinor, CurrencyCode(currencyCode)), TransactionType.valueOf(type), accountId,
    destinationAccountId, categoryId, description, notes, Instant.ofEpochMilli(occurredAtEpochMillis),
    Instant.ofEpochMilli(createdAtEpochMillis),
)
internal fun CategoryEntity.toDomain() = Category(id, name, iconKey, isDefault)
internal fun Category.toEntity() = CategoryEntity(id, name, iconKey, isDefault)
internal fun BudgetEntity.toDomain() = Budget(id, categoryId, YearMonth.parse(month), Money(limitMinor, CurrencyCode(currencyCode)))
internal fun SavingsGoalEntity.toDomain() = SavingsGoal(
    id, name, Money(targetMinor, CurrencyCode(currencyCode)), Money(savedMinor, CurrencyCode(currencyCode)),
    targetEpochDay?.let(LocalDate::ofEpochDay),
)
