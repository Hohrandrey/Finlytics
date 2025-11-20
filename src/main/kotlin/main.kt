import db.database_request.*
import java.sql.SQLException

fun main() {
    try {
        println("FINLYTICS - СИСТЕМА УПРАВЛЕНИЯ ФИНАНСАМИ")

        interactiveMenu()

    } catch (e: SQLException) {
        println("Критическая ошибка базы данных: ${e.message}")
        println("Проверьте наличие файла базы данных и права доступа.")
    } catch (e: Exception) {
        println("Неожиданная ошибка: ${e.message}")
        e.printStackTrace()
    }
}

fun interactiveMenu() {
    while (true) {
        try {
            println("\n=== ГЛАВНОЕ МЕНЮ ===")
            println("1. Добавить")
            println("2. Удалить")
            println("3. Показать")
            println("0. Выход")

            print("Выберите действие: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> addMenu()
                "2" -> deleteMenu()
                "3" -> showMenu()
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

fun addMenu() {
    while (true) {
        try {
            println("\n=== ДОБАВЛЕНИЕ ===")
            println("1. Категории")
            println("2. Транзакции")
            println("0. Назад")

            print("Выберите тип данных: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> addCategoriesMenu()
                "2" -> addTransactionsMenu()
                "0" -> break
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню добавления: ${e.message}")
        }
    }
}

fun deleteMenu() {
    while (true) {
        try {
            println("\n=== УДАЛЕНИЕ ===")
            println("1. Категории")
            println("2. Транзакции")
            println("0. Назад")

            print("Выберите тип данных: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> deleteCategoriesMenu()
                "2" -> deleteTransactionsMenu()
                "0" -> break
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню удаления: ${e.message}")
        }
    }
}

fun showMenu() {
    while (true) {
        try {
            println("\n=== ПОКАЗАТЬ ===")
            println("1. Категории")
            println("2. Транзакции")
            println("0. Назад")

            print("Выберите тип данных: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> showCategoriesMenu()
                "2" -> showTransactionsMenu()
                "0" -> break
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню показа: ${e.message}")
        }
    }
}

// Подменю для добавления
fun addCategoriesMenu() {
    while (true) {
        try {
            println("\n=== ДОБАВЛЕНИЕ КАТЕГОРИЙ ===")
            println("1. Добавить категорию доходов")
            println("2. Добавить категорию расходов")
            println("0. Назад")

            print("Выберите действие: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> AddCategory.addIncomeCategoryInteractive()
                "2" -> AddCategory.addExpensesCategoryInteractive()
                "0" -> break
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню добавления категорий: ${e.message}")
        }
    }
}

fun addTransactionsMenu() {
    while (true) {
        try {
            println("\n=== ДОБАВЛЕНИЕ ТРАНЗАКЦИЙ ===")
            println("1. Добавить транзакцию доходов")
            println("2. Добавить транзакцию расходов")
            println("0. Назад")

            print("Выберите действие: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> AddIncomeTransaction.addIncomeTransactionInteractive()
                "2" -> AddExpensesTransaction.addExpensesTransactionInteractive()
                "0" -> break
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню добавления транзакций: ${e.message}")
        }
    }
}

// Подменю для удаления
fun deleteCategoriesMenu() {
    while (true) {
        try {
            println("\n=== УДАЛЕНИЕ КАТЕГОРИЙ ===")
            println("1. Удалить категорию доходов")
            println("2. Удалить категорию расходов")
            println("0. Назад")

            print("Выберите действие: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> DeleteCategory.deleteIncomeCategoryInteractive()
                "2" -> DeleteCategory.deleteExpensesCategoryInteractive()
                "0" -> break
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню удаления категорий: ${e.message}")
        }
    }
}

fun deleteTransactionsMenu() {
    while (true) {
        try {
            println("\n=== УДАЛЕНИЕ ТРАНЗАКЦИЙ ===")
            println("1. Удалить транзакцию доходов")
            println("2. Удалить транзакцию расходов")
            println("0. Назад")

            print("Выберите действие: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> DeleteIncomeTransaction.deleteIncomeTransactionInteractive()
                "2" -> DeleteExpensesTransaction.deleteExpensesTransactionInteractive()
                "0" -> break
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню удаления транзакций: ${e.message}")
        }
    }
}

// Подменю для показа
fun showCategoriesMenu() {
    while (true) {
        try {
            println("\n=== ПОКАЗАТЬ КАТЕГОРИИ ===")
            println("1. Показать категории доходов")
            println("2. Показать категории расходов")
            println("3. Показать все категории")
            println("0. Назад")

            print("Выберите действие: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> DeleteCategory.showIncomeCategories()
                "2" -> DeleteCategory.showExpensesCategories()
                "3" -> DeleteCategory.showAllCategories()
                "0" -> break
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню показа категорий: ${e.message}")
        }
    }
}

fun showTransactionsMenu() {
    while (true) {
        try {
            println("\n=== ПОКАЗАТЬ ТРАНЗАКЦИИ ===")
            println("1. Показать транзакции доходов")
            println("2. Показать транзакции расходов")
            println("3. Показать все транзакции")
            println("0. Назад")

            print("Выберите действие: ")
            val choice = readLine()?.trim()

            when (choice) {
                "1" -> DeleteIncomeTransaction.showIncomeTransactions()
                "2" -> DeleteExpensesTransaction.showExpensesTransactions()
                "3" -> {
                    DeleteIncomeTransaction.showIncomeTransactions()
                    DeleteExpensesTransaction.showExpensesTransactions()
                }
                "0" -> break
                else -> println("Неверный выбор, попробуйте снова")
            }
        } catch (e: Exception) {
            println("Ошибка в меню показа транзакций: ${e.message}")
        }
    }
}