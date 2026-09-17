package com.moneytracker.android.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moneytracker.android.R
import com.moneytracker.android.ui.accounts.AccountsScreen
import com.moneytracker.android.ui.analytics.AnalyticsScreen
import com.moneytracker.android.ui.dashboard.DashboardScreen
import com.moneytracker.android.ui.navigation.Destination
import com.moneytracker.android.ui.settings.SettingsScreen
import com.moneytracker.android.ui.transactions.TransactionsScreen

@Composable
fun MoneyTrackerApp(viewModel: ShellViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MoneyTrackerShell(state, onRetry = viewModel::retry)
}

@Composable
fun MoneyTrackerShell(state: FoundationState, onRetry: () -> Unit) {
    val controller = rememberNavController()
    val entry by controller.currentBackStackEntryAsState()
    val route = entry?.destination?.route ?: Destination.HOME.route
    val navigate: (Destination) -> Unit = { destination ->
        controller.navigate(destination.route) {
            popUpTo(controller.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val expanded = maxWidth >= 600.dp
        Row(Modifier.fillMaxSize()) {
            if (expanded) {
                NavigationRail {
                    Destination.entries.forEach { destination ->
                        NavigationRailItem(
                            selected = route == destination.route,
                            onClick = { navigate(destination) },
                            icon = { Icon(painterResource(destination.icon), contentDescription = null) },
                            label = { Text(stringResource(destination.label)) },
                        )
                    }
                }
            }
            Scaffold(
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    if (!expanded && route != "settings") {
                        NavigationBar {
                            Destination.entries.forEach { destination ->
                                NavigationBarItem(
                                    selected = route == destination.route,
                                    onClick = { navigate(destination) },
                                    icon = { Icon(painterResource(destination.icon), contentDescription = null) },
                                    label = { Text(stringResource(destination.label)) },
                                )
                            }
                        }
                    }
                },
            ) { padding ->
                NavHost(controller, startDestination = Destination.HOME.route, modifier = Modifier.padding(padding)) {
                    composable(Destination.HOME.route) {
                        DashboardScreen(state, onRetry, onSettings = { controller.navigate("settings") { launchSingleTop = true } })
                    }
                    composable(Destination.ACCOUNTS.route) { AccountsScreen() }
                    composable(Destination.TRANSACTIONS.route) { TransactionsScreen() }
                    composable(Destination.ANALYTICS.route) { AnalyticsScreen() }
                    composable("settings") { SettingsScreen(onBack = { controller.popBackStack() }) }
                }
            }
        }
    }
}
