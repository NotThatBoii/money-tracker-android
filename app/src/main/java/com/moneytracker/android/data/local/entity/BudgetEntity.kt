package com.moneytracker.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budgets",
    foreignKeys = [ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["categoryId"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index(value = ["categoryId", "month", "currencyCode"], unique = true)],
)
data class BudgetEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    /** ISO YearMonth, e.g. 2026-09; month boundaries use the user's local calendar. */
    val month: String,
    val limitMinor: Long,
    val currencyCode: String,
)
