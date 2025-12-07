package db.database_request

object DefaultCategories {
    private val defaultIncomeCategories = listOf(
        "Зарплата",
        "Стипендия",
        "Фриланс",
        "Инвестиции",
        "Подарок"
    )

    private val defaultExpenseCategories = listOf(
        "Еда",
        "Транспорт",
        "Жилье",
        "Развлечения",
        "Образование",
        "Здоровье",
        "Одежда",
        "Коммунальные услуги"
    )

    fun initializeDefaultCategories() {
        println("Инициализация категорий по умолчанию...")

        var incomeCount = 0
        defaultIncomeCategories.forEach { category ->
            if (!incomeCategoryExists(category)) {
                val success = AddCategory.addIncomeCategory(category)
                if (success) incomeCount++
            }
        }
        println("Добавлено категорий доходов: $incomeCount")

        var expenseCount = 0
        defaultExpenseCategories.forEach { category ->
            if (!expenseCategoryExists(category)) {
                val success = AddCategory.addExpensesCategory(category)
                if (success) expenseCount++
            }
        }
        println("Добавлено категорий расходов: $expenseCount")
    }

    private fun incomeCategoryExists(name: String): Boolean {
        return GetIncomeCategories.getAllNames().any { it.equals(name, ignoreCase = true) }
    }

    private fun expenseCategoryExists(name: String): Boolean {
        return GetExpensesCategories.getAllNames().any { it.equals(name, ignoreCase = true) }
    }
}
