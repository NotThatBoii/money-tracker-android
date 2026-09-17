package com.moneytracker.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.moneytracker.android.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets ORDER BY month DESC, categoryId, id")
    fun observeAll(): Flow<List<BudgetEntity>>
    @Insert
    suspend fun insert(budget: BudgetEntity)
}
