package com.moneytracker.android.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.moneytracker.android.R

enum class Destination(val route: String, @get:StringRes val label: Int, @get:DrawableRes val icon: Int) {
    HOME("home", R.string.nav_home, R.drawable.ic_home),
    TRANSACTIONS("transactions", R.string.nav_transactions, R.drawable.ic_transactions),
    ACCOUNTS("accounts", R.string.nav_accounts, R.drawable.ic_accounts),
    ANALYTICS("analytics", R.string.nav_analytics, R.drawable.ic_analytics),
}
