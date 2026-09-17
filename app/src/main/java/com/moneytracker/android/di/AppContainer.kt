package com.moneytracker.android.di

import android.content.Context
import com.moneytracker.android.data.local.MoneyTrackerDatabase
import com.moneytracker.android.data.repository.RoomAccountRepository
import com.moneytracker.android.data.repository.RoomBudgetRepository
import com.moneytracker.android.data.repository.RoomCategoryRepository
import com.moneytracker.android.data.repository.RoomDatabaseInitializer
import com.moneytracker.android.data.repository.RoomSavingsGoalRepository
import com.moneytracker.android.data.repository.RoomTransactionRepository
import com.moneytracker.android.domain.repository.AccountRepository
import com.moneytracker.android.domain.repository.BudgetRepository
import com.moneytracker.android.domain.repository.CategoryRepository
import com.moneytracker.android.domain.repository.DatabaseInitializer
import com.moneytracker.android.domain.repository.SavingsGoalRepository
import com.moneytracker.android.domain.repository.TransactionRepository

/** Application-scoped dependencies; UI only receives repository interfaces. */
class AppContainer(context: Context) {
    private val database = MoneyTrackerDatabase.create(context)
    val accounts: AccountRepository = RoomAccountRepository(database.accountDao())
    val transactions: TransactionRepository = RoomTransactionRepository(database.transactionDao())
    val categories: CategoryRepository = RoomCategoryRepository(database.categoryDao())
    val budgets: BudgetRepository = RoomBudgetRepository(database.budgetDao())
    val goals: SavingsGoalRepository = RoomSavingsGoalRepository(database.savingsGoalDao())
    val initializer: DatabaseInitializer = RoomDatabaseInitializer(database)
}
