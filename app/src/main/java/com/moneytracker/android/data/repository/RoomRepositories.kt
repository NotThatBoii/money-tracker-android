package com.moneytracker.android.data.repository

import androidx.room.withTransaction
import com.moneytracker.android.data.local.MoneyTrackerDatabase
import com.moneytracker.android.data.local.dao.AccountDao
import com.moneytracker.android.data.local.dao.BudgetDao
import com.moneytracker.android.data.local.dao.CategoryDao
import com.moneytracker.android.data.local.dao.SavingsGoalDao
import com.moneytracker.android.data.local.dao.TransactionDao
import com.moneytracker.android.domain.model.DefaultCategories
import com.moneytracker.android.domain.repository.AccountRepository
import com.moneytracker.android.domain.repository.BudgetRepository
import com.moneytracker.android.domain.repository.CategoryRepository
import com.moneytracker.android.domain.repository.DatabaseInitializer
import com.moneytracker.android.domain.repository.SavingsGoalRepository
import com.moneytracker.android.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.map

class RoomAccountRepository(private val dao: AccountDao) : AccountRepository {
    override fun observeAccounts() = dao.observeAll().map { rows -> rows.map { it.toDomain() } }
}
class RoomTransactionRepository(private val dao: TransactionDao) : TransactionRepository {
    override fun observeTransactions() = dao.observeAll().map { rows -> rows.map { it.toDomain() } }
}
class RoomCategoryRepository(private val dao: CategoryDao) : CategoryRepository {
    override fun observeCategories() = dao.observeAll().map { rows -> rows.map { it.toDomain() } }
}
class RoomBudgetRepository(private val dao: BudgetDao) : BudgetRepository {
    override fun observeBudgets() = dao.observeAll().map { rows -> rows.map { it.toDomain() } }
}
class RoomSavingsGoalRepository(private val dao: SavingsGoalDao) : SavingsGoalRepository {
    override fun observeGoals() = dao.observeAll().map { rows -> rows.map { it.toDomain() } }
}
class RoomDatabaseInitializer(private val database: MoneyTrackerDatabase) : DatabaseInitializer {
    override suspend fun initialize() = database.withTransaction {
        // Reference categories only. Never insert sample accounts or money in production startup.
        database.categoryDao().insertDefaults(DefaultCategories.all.map { it.toEntity() })
    }
}
