package com.moneytracker.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.moneytracker.android.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY isArchived, name COLLATE NOCASE, id")
    fun observeAll(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun findById(id: String): AccountEntity?

    @Insert
    suspend fun insert(account: AccountEntity)
}
