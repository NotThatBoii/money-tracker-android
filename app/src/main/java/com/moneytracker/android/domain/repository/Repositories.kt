package com.moneytracker.android.domain.repository

import com.moneytracker.android.domain.model.Account
import com.moneytracker.android.domain.model.Budget
import com.moneytracker.android.domain.model.Category
import com.moneytracker.android.domain.model.FinancialTransaction
import com.moneytracker.android.domain.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

// Phase 1 exposes reads only. Validated write operations arrive with each feature phase.
interface AccountRepository { fun observeAccounts(): Flow<List<Account>> }
interface TransactionRepository { fun observeTransactions(): Flow<List<FinancialTransaction>> }
interface CategoryRepository { fun observeCategories(): Flow<List<Category>> }
interface BudgetRepository { fun observeBudgets(): Flow<List<Budget>> }
interface SavingsGoalRepository { fun observeGoals(): Flow<List<SavingsGoal>> }
interface DatabaseInitializer { suspend fun initialize() }
