package com.moneytracker.android

import android.app.Application
import com.moneytracker.android.di.AppContainer

class MoneyTrackerApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
