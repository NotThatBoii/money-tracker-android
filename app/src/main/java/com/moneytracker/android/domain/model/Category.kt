package com.moneytracker.android.domain.model

data class Category(val id: String, val name: String, val iconKey: String, val isDefault: Boolean) {
    init { require(id.isNotBlank() && name.isNotBlank() && iconKey.isNotBlank()) }
}

/** Stable identifiers are independent of display names and future localization. */
object DefaultCategories {
    val all: List<Category> = listOf(
        "food" to "Food", "transportation" to "Transportation", "school" to "School",
        "shopping" to "Shopping", "bills" to "Bills", "subscriptions" to "Subscriptions",
        "entertainment" to "Entertainment", "health" to "Health", "travel" to "Travel",
        "family" to "Family", "salary" to "Salary", "allowance" to "Allowance",
        "freelance" to "Freelance", "savings" to "Savings", "other" to "Other",
    ).map { (key, name) -> Category(key, name, key, isDefault = true) }
}
