package com.moneytracker.android.data

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import com.moneytracker.android.data.local.MoneyTrackerDatabase
import com.moneytracker.android.data.local.entity.AccountEntity
import com.moneytracker.android.data.local.entity.BudgetEntity
import com.moneytracker.android.data.local.entity.SavingsGoalEntity
import com.moneytracker.android.data.local.entity.TransactionEntity
import com.moneytracker.android.data.repository.*
import com.moneytracker.android.domain.model.Money
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.SQLiteMode
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@SQLiteMode(SQLiteMode.Mode.NATIVE)
class RoomPersistenceTest {
    private lateinit var database: MoneyTrackerDatabase
    @Before fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), MoneyTrackerDatabase::class.java).build()
    }
    @After fun close() { database.close() }
    private fun account(id: String = "a") = AccountEntity(id, "BPI", "BPI", "BANK", 12345, "PHP", "1234", null, null, 1000, false)
    private fun transaction(id: String = "t", accountId: String = "a", fingerprint: String? = null) =
        TransactionEntity(id, 100, "PHP", "EXPENSE", accountId, null, "food", "Lunch", null, 1000, 2000, fingerprint = fingerprint)

    @Test fun initializationIsIdempotentAndNeverSeedsFinancialData() = runBlocking {
        val initializer = RoomDatabaseInitializer(database)
        initializer.initialize()
        initializer.initialize()
        assertEquals(15, RoomCategoryRepository(database.categoryDao()).observeCategories().first().size)
        assertTrue(RoomAccountRepository(database.accountDao()).observeAccounts().first().isEmpty())
        assertTrue(RoomTransactionRepository(database.transactionDao()).observeTransactions().first().isEmpty())
        assertTrue(RoomBudgetRepository(database.budgetDao()).observeBudgets().first().isEmpty())
        assertTrue(RoomSavingsGoalRepository(database.savingsGoalDao()).observeGoals().first().isEmpty())
    }
    @Test fun repositoryMapsExactBalancesAndTimestamps() = runBlocking {
        database.accountDao().insert(account())
        val result = RoomAccountRepository(database.accountDao()).observeAccounts().first().single()
        assertEquals(Money(12345), result.balance)
        assertEquals(1000L, result.updatedAt.toEpochMilli())
        assertEquals("1234", result.lastFour)
    }
    @Test fun failedTransactionRollsBackEarlierWrites() = runBlocking {
        RoomDatabaseInitializer(database).initialize()
        try {
            database.withTransaction {
                database.accountDao().insert(account())
                database.transactionDao().insert(transaction(accountId = "missing"))
            }
            fail("Foreign key failure expected")
        } catch (_: android.database.sqlite.SQLiteConstraintException) {
            assertNull(database.accountDao().findById("a"))
            assertTrue(database.transactionDao().observeAll().first().isEmpty())
        }
    }
    @Test fun referencedAccountsCannotBeDeleted() = runBlocking {
        RoomDatabaseInitializer(database).initialize()
        database.accountDao().insert(account())
        database.transactionDao().insert(transaction())
        try {
            database.openHelper.writableDatabase.execSQL("DELETE FROM accounts WHERE id = 'a'")
            fail("Referenced account deletion must fail")
        } catch (_: android.database.sqlite.SQLiteConstraintException) {
            assertNotNull(database.accountDao().findById("a"))
        }
    }
    @Test fun duplicateImportFingerprintIsRejectedButManualDuplicatesAreAllowed() = runBlocking {
        RoomDatabaseInitializer(database).initialize()
        database.accountDao().insert(account())
        database.transactionDao().insert(transaction("manual-1"))
        database.transactionDao().insert(transaction("manual-2"))
        database.transactionDao().insert(transaction("import-1", fingerprint = "stable-fingerprint"))
        try {
            database.transactionDao().insert(transaction("import-2", fingerprint = "stable-fingerprint"))
            fail("Duplicate fingerprint expected")
        } catch (_: android.database.sqlite.SQLiteConstraintException) {
            assertEquals(3, database.transactionDao().observeAll().first().size)
        }
    }
    @Test fun monthlyBudgetIsUniquePerCategoryAndCurrency() = runBlocking {
        RoomDatabaseInitializer(database).initialize()
        val budget = BudgetEntity("b", "food", "2026-09", 400000, "PHP")
        database.budgetDao().insert(budget)
        try {
            database.budgetDao().insert(budget.copy(id = "duplicate"))
            fail("Duplicate category/month budget expected")
        } catch (_: android.database.sqlite.SQLiteConstraintException) {
            assertEquals(Money(400000), RoomBudgetRepository(database.budgetDao()).observeBudgets().first().single().limit)
        }
    }
    @Test fun savingsGoalDateRoundTripsWithoutTimeZoneShift() = runBlocking {
        val date = LocalDate.of(2027, 1, 15)
        database.savingsGoalDao().insert(SavingsGoalEntity("g", "Laptop", 6000000, 3000000, "PHP", date.toEpochDay()))
        val goal = RoomSavingsGoalRepository(database.savingsGoalDao()).observeGoals().first().single()
        assertEquals(date, goal.targetDate)
        assertEquals(Money(3000000), goal.saved)
    }
    @Test fun databaseSurvivesCloseAndReopen() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val name = "persistence-test.db"
        context.deleteDatabase(name)
        var disk = Room.databaseBuilder(context, MoneyTrackerDatabase::class.java, name).build()
        try {
            disk.accountDao().insert(account())
            disk.close()
            disk = Room.databaseBuilder(context, MoneyTrackerDatabase::class.java, name).build()
            assertEquals(12345L, disk.accountDao().findById("a")?.balanceMinor)
        } finally {
            disk.close()
            context.deleteDatabase(name)
        }
    }
}
