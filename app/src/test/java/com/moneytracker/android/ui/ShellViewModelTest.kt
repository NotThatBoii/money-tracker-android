package com.moneytracker.android.ui

import com.moneytracker.android.domain.model.Category
import com.moneytracker.android.domain.model.DefaultCategories
import com.moneytracker.android.domain.repository.CategoryRepository
import com.moneytracker.android.domain.repository.DatabaseInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShellViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val categories = object : CategoryRepository {
        override fun observeCategories() = flowOf<List<Category>>(DefaultCategories.all)
    }
    @Before fun setup() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test fun successfulInitializationExposesReadyState() = runTest(dispatcher) {
        val model = ShellViewModel(object : DatabaseInitializer { override suspend fun initialize() {} }, categories)
        assertEquals(FoundationState.Loading, model.state.value)
        advanceUntilIdle()
        assertEquals(FoundationState.Ready(15), model.state.value)
    }
    @Test fun failureIsSafeAndCanBeRetried() = runTest(dispatcher) {
        var attempts = 0
        val model = ShellViewModel(object : DatabaseInitializer {
            override suspend fun initialize() { if (++attempts == 1) error("Internal details") }
        }, categories)
        advanceUntilIdle()
        assertEquals(FoundationState.Error, model.state.value)
        model.retry()
        advanceUntilIdle()
        assertEquals(FoundationState.Ready(15), model.state.value)
    }
}
