import db.database_request.AddCategory
import db.database_request.DeleteCategory
import java.sql.SQLException

fun main() {
    try {
        println("FINLYTICS")

        // Интерактивный режим для ручного управления
        interactiveMenu()

    } catch (e: SQLException) {
        println("❌ Критическая ошибка базы данных: ${e.message}")
        println("Проверьте наличие файла базы данных и права доступа.")
    } catch (e: Exception) {
        println("❌ Неожиданная ошибка: ${e.message}")
        e.printStackTrace()
    }
}

fun interactiveMenu() {
    while (true) {
        try {
            println("\n=== ГЛАВНОЕ МЕНЮ ===")
            println("1. Добавить категорию")
            println("2. Удалить категорию")
            println("3. Показать все категории")
            println("0. Выход")

            print("Выберите действие: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> {
                    try {
                        AddCategory.addCategoryInteractive()
                    } catch (e: Exception) {
                        println("Ошибка при добавлении категории: ${e.message}")
                    }
                }
                "2" -> {
                    try {
                        DeleteCategory.deleteCategoryInteractive()
                    } catch (e: Exception) {
                        println("Ошибка при удалении категории: ${e.message}")
                    }
                }
                "3" -> {
                    try {
                        if (!DeleteCategory.showAllCategories()) {
                            println("Не удалось загрузить список категорий")
                        }
                    } catch (e: Exception) {
                        println("Ошибка при отображении категорий: ${e.message}")
                    }
                }
                "0" -> {
                    println("Выход из программы...")
                    break
                }
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню: ${e.message}")
        }
    }
}