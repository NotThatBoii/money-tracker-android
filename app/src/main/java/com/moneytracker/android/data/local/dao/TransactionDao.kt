package com.moneytracker.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.moneytracker.android.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY occurredAtEpochMillis DESC, createdAtEpochMillis DESC, id")
    fun observeAll(): Flow<List<TransactionEntity>>

    // Persistence primitive only. Phase 3 will wrap balance writes and this insert in withTransaction.
    @Insert
    suspend fun insert(transaction: TransactionEntity)
}
