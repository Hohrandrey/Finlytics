package db.database_request

/**
 * Предоставляет стандартные категории доходов и расходов.
 * Инициализирует их при первом запуске приложения.
 *
 * Стандартные категории:
 * - Доходы: Зарплата, Стипендия, Фриланс, Инвестиции, Подарок
 * - Расходы: Еда, Транспорт, Жилье, Развлечения, Образование, Здоровье, Одежда, Коммунальные услуги
 *
 * @author Finlytics Team
 * @since 1.0.0
 */
object DefaultCategories {
    // Стандартные категории доходов
    private val defaultIncomeCategories = listOf(
        "Зарплата",
        "Стипендия",
        "Фриланс",
        "Инвестиции",
        "Подарок"
    )

    // Стандартные категории расходов
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

    /**
     * Добавляет стандартные категории в базу данных, если их еще нет.
     * Вызывается при инициализации базы данных.
     * Каждая категория добавляется только если она еще не существует в базе.
     */
    fun initializeDefaultCategories() {
        println("Инициализация категорий по умолчанию...")

        // Добавляем категории доходов
        var incomeCount = 0
        defaultIncomeCategories.forEach { category ->
            if (!incomeCategoryExists(category)) {
                val success = AddCategory.addIncomeCategory(category)
                if (success) incomeCount++
            }
        }
        println("Добавлено категорий доходов: $incomeCount")

        // Добавляем категории расходов
        var expenseCount = 0
        defaultExpenseCategories.forEach { category ->
            if (!expenseCategoryExists(category)) {
                val success = AddCategory.addExpensesCategory(category)
                if (success) expenseCount++
            }
        }
        println("Добавлено категорий расходов: $expenseCount")
    }

    /**
     * Проверяет существование категории доходов.
     *
     * @param name Название категории для проверки
     * @return true если категория существует, false в противном случае
     */
    private fun incomeCategoryExists(name: String): Boolean {
        return GetIncomeCategories.getAllNames().any { it.equals(name, ignoreCase = true) }
    }

    /**
     * Проверяет существование категории расходов.
     *
     * @param name Название категории для проверки
     * @return true если категория существует, false в противном случае
     */
    private fun expenseCategoryExists(name: String): Boolean {
        return GetExpensesCategories.getAllNames().any { it.equals(name, ignoreCase = true) }
    }
}