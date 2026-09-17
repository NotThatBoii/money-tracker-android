package com.moneytracker.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.moneytracker.android.data.local.entity.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsGoalDao {
    @Query("SELECT * FROM savings_goals ORDER BY name COLLATE NOCASE, id")
    fun observeAll(): Flow<List<SavingsGoalEntity>>
    @Insert
    suspend fun insert(goal: SavingsGoalEntity)
}
