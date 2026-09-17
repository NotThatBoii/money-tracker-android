package com.moneytracker.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(entity = AccountEntity::class, parentColumns = ["id"], childColumns = ["accountId"], onDelete = ForeignKey.RESTRICT),
        ForeignKey(entity = AccountEntity::class, parentColumns = ["id"], childColumns = ["destinationAccountId"], onDelete = ForeignKey.RESTRICT),
        ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["categoryId"], onDelete = ForeignKey.RESTRICT),
    ],
    indices = [
        Index(value = ["accountId", "occurredAtEpochMillis"]),
        Index(value = ["destinationAccountId"]), Index(value = ["categoryId"]),
        Index(value = ["occurredAtEpochMillis"]),
        Index(value = ["accountId", "sourceKey", "externalId"], unique = true),
        Index(value = ["accountId", "sourceKey", "fingerprint"], unique = true),
    ],
)
data class TransactionEntity(
    @PrimaryKey val id: String,
    val amountMinor: Long,
    val currencyCode: String,
    val type: String,
    val accountId: String,
    val destinationAccountId: String?,
    val categoryId: String?,
    val description: String,
    val notes: String?,
    val occurredAtEpochMillis: Long,
    val createdAtEpochMillis: Long,
    // Non-secret provenance for future imports. NULL permits distinct identical manual entries.
    val sourceKey: String = "manual",
    val externalId: String? = null,
    val fingerprint: String? = null,
)
