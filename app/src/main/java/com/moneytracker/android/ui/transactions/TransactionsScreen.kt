package com.moneytracker.android.ui.transactions

import androidx.compose.runtime.Composable
import com.moneytracker.android.R
import com.moneytracker.android.ui.components.FeatureScaffold

@Composable
fun TransactionsScreen() = FeatureScaffold(R.string.nav_transactions, R.string.transactions_pending, R.drawable.ic_transactions)
