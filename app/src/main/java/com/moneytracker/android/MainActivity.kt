package com.moneytracker.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneytracker.android.ui.MoneyTrackerApp
import com.moneytracker.android.ui.ShellViewModel
import com.moneytracker.android.ui.theme.MoneyTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as MoneyTrackerApplication).container
        setContent {
            MoneyTrackerTheme {
                val model: ShellViewModel = viewModel(factory = ShellViewModel.factory(container.initializer, container.categories))
                MoneyTrackerApp(model)
            }
        }
    }
}
