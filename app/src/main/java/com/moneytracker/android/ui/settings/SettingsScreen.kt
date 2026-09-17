package com.moneytracker.android.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moneytracker.android.R

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        TextButton(onClick = onBack) { Text(stringResource(R.string.back_home)) }
        Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineLarge)
        Text(stringResource(R.string.appearance_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.appearance_body))
        Text(stringResource(R.string.security_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.privacy_note))
        Text(stringResource(R.string.lock_pending), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
