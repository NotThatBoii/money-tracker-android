package com.moneytracker.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val targetMinor: Long,
    val savedMinor: Long,
    val currencyCode: String,
    /** Epoch day preserves the date across time zones. */
    val targetEpochDay: Long?,
)
