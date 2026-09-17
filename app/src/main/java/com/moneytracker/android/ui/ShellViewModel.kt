package com.moneytracker.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.moneytracker.android.domain.repository.CategoryRepository
import com.moneytracker.android.domain.repository.DatabaseInitializer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface FoundationState {
    data object Loading : FoundationState
    data class Ready(val categoryCount: Int) : FoundationState
    data object Error : FoundationState
}

class ShellViewModel(
    private val initializer: DatabaseInitializer,
    private val categories: CategoryRepository,
) : ViewModel() {
    private val mutableState = MutableStateFlow<FoundationState>(FoundationState.Loading)
    val state = mutableState.asStateFlow()
    private var initialization: Job? = null

    init { retry() }

    fun retry() {
        if (initialization?.isActive == true) return
        initialization = viewModelScope.launch {
            mutableState.value = FoundationState.Loading
            try {
                initializer.initialize()
                categories.observeCategories().collect { mutableState.value = FoundationState.Ready(it.size) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                // Never expose SQL, transaction details, or financial values through logs/error text.
                mutableState.value = FoundationState.Error
            }
        }
    }

    companion object {
        fun factory(databaseInitializer: DatabaseInitializer, categories: CategoryRepository): ViewModelProvider.Factory =
            viewModelFactory { initializer { ShellViewModel(databaseInitializer, categories) } }
    }
}
