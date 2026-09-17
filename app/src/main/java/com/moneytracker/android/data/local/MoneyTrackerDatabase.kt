package com.moneytracker.android.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.moneytracker.android.data.local.dao.AccountDao
import com.moneytracker.android.data.local.dao.BudgetDao
import com.moneytracker.android.data.local.dao.CategoryDao
import com.moneytracker.android.data.local.dao.SavingsGoalDao
import com.moneytracker.android.data.local.dao.TransactionDao
import com.moneytracker.android.data.local.entity.AccountEntity
import com.moneytracker.android.data.local.entity.BudgetEntity
import com.moneytracker.android.data.local.entity.CategoryEntity
import com.moneytracker.android.data.local.entity.SavingsGoalEntity
import com.moneytracker.android.data.local.entity.TransactionEntity

@Database(
    entities = [AccountEntity::class, TransactionEntity::class, CategoryEntity::class, BudgetEntity::class, SavingsGoalEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class MoneyTrackerDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun savingsGoalDao(): SavingsGoalDao

    companion object {
        // Initial schema has no predecessor. Every version bump must register and test a migration.
        val MIGRATIONS: Array<Migration> = emptyArray()

        fun create(context: Context): MoneyTrackerDatabase = Room.databaseBuilder(
            context.applicationContext, MoneyTrackerDatabase::class.java, "money-tracker.db",
        ).addMigrations(*MIGRATIONS)
            // Never enable destructive migration: a missing migration must fail closed.
            .build()
    }
}
