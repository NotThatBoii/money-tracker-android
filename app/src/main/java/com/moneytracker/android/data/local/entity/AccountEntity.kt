package com.moneytracker.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val institution: String,
    val type: String,
    val balanceMinor: Long,
    val currencyCode: String,
    val lastFour: String?,
    val iconKey: String?,
    val colorArgb: Long?,
    val updatedAtEpochMillis: Long,
    val isArchived: Boolean,
)
