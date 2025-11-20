
fun main() {
    println("=== FINLYTICS - УПРАВЛЕНИЕ КАТЕГОРИЯМИ ===")

    // Автоматическое добавление стандартных категорий при запуске
    println("Инициализация стандартных категорий...")
    AddCategory.addDefaultCategories()

    // Добавление дополнительных категорий
    println("\nДобавление дополнительных категорий...")
    AddCategory.addExpensesCategory("Рестораны")
    AddCategory.addExpensesCategory("Путешествия")
    AddCategory.addIncomeCategory("Аренда")
    AddCategory.addIncomeCategory("Бизнес")

    // Показать все добавленные категории
    println("\nТекущие категории в системе:")
    DeleteCategory.showAllCategories()

    // Интерактивный режим для ручного управления
    while (true) {
        println("\n=== ГЛАВНОЕ МЕНЮ ===")
        println("1. Добавить категорию")
        println("2. Удалить категорию")
        println("3. Показать все категории")
        println("4. Добавить стандартные категории")
        println("0. Выход")

        print("Выберите действие: ")
        val choice = readLine()?.trim()

        when (choice) {
            "1" -> {
                AddCategory.addCategoryInteractive()
            }
            "2" -> {
                DeleteCategory.deleteCategoryInteractive()
            }
            "3" -> {
                DeleteCategory.showAllCategories()
            }
            "4" -> {
                AddCategory.addDefaultCategories()
            }
            "0" -> {
                println("Выход из программы...")
                break
            }
            else -> println("❌ Неверный выбор, попробуйте снова")
        }
    }
}