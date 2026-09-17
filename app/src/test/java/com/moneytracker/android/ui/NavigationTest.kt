package com.moneytracker.android.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.moneytracker.android.ui.theme.MoneyTrackerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "w411dp-h891dp")
class NavigationTest {
    @get:Rule val compose = createComposeRule()

    @Test fun allTabsAndSettingsAreReachable() {
        compose.setContent { MoneyTrackerTheme { MoneyTrackerShell(FoundationState.Ready(15), {}) } }
        compose.onNodeWithText("Local database ready").assertIsDisplayed()
        compose.onNodeWithText("Accounts").performClick()
        compose.onNodeWithText("Your accounts will live here.", substring = true).assertIsDisplayed()
        compose.onNodeWithText("Transactions").performClick()
        compose.onNodeWithText("Income, expenses, and transfers", substring = true).assertIsDisplayed()
        compose.onNodeWithText("Analytics").performClick()
        compose.onNodeWithText("Spending trends", substring = true).assertIsDisplayed()
        compose.onNodeWithText("Home").performClick()
        compose.onNodeWithContentDescription("Settings").performClick()
        compose.onNodeWithText("Privacy and security").assertIsDisplayed()
        compose.onNodeWithText("Back to Home").performClick()
        compose.onNodeWithText("Local database ready").assertIsDisplayed()
    }
    @Test fun darkThemeRendersErrorAndAllowsRetry() {
        var retries = 0
        compose.setContent { MoneyTrackerTheme(darkTheme = true) { MoneyTrackerShell(FoundationState.Error, { retries++ }) } }
        compose.onNodeWithText("Try again").performClick()
        org.junit.Assert.assertEquals(1, retries)
    }
}
