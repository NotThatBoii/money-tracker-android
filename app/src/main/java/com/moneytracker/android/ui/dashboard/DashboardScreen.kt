package com.moneytracker.android.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moneytracker.android.R
import com.moneytracker.android.ui.FoundationState

@Composable
fun DashboardScreen(state: FoundationState, onRetry: () -> Unit, onSettings: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(Modifier.widthIn(max = 720.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.app_name), Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = onSettings) {
                    Icon(painterResource(R.drawable.ic_settings), stringResource(R.string.settings))
                }
            }
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.fillMaxWidth().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(stringResource(R.string.local_first), style = MaterialTheme.typography.labelLarge)
                    Text(stringResource(R.string.welcome_title), style = MaterialTheme.typography.headlineLarge)
                    Text(stringResource(R.string.welcome_body), style = MaterialTheme.typography.bodyLarge)
                }
            }
            Text(stringResource(R.string.foundation_label), style = MaterialTheme.typography.titleLarge)
            Card {
                Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    when (state) {
                        FoundationState.Loading -> {
                            CircularProgressIndicator()
                            Text(stringResource(R.string.database_loading))
                        }
                        is FoundationState.Ready -> {
                            Text(stringResource(R.string.database_ready), style = MaterialTheme.typography.titleMedium)
                            Text(pluralStringResource(R.plurals.categories_ready, state.categoryCount, state.categoryCount))
                            Text(stringResource(R.string.dashboard_pending), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        FoundationState.Error -> {
                            Text(stringResource(R.string.database_error), color = MaterialTheme.colorScheme.error)
                            Button(onClick = onRetry) { Text(stringResource(R.string.retry)) }
                        }
                    }
                }
            }
            Text(stringResource(R.string.privacy_note), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
