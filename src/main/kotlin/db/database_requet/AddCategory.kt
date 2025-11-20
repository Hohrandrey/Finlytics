package db.database_requet

import java.sql.DriverManager
import java.sql.SQLException

class AddCategory {
    companion object {
        private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

        // Добавление категории расходов
        fun addExpensesCategory(categoryName: String): Boolean {
            val sql = "INSERT INTO expenses_categories (expenses_category_name) VALUES (?)"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setString(1, categoryName)

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("Категория расходов '$categoryName' успешно добавлена!")
                            true
                        } else {
                            println("Ошибка при добавлении категории расходов")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("UNIQUE constraint failed") == true -> {
                        println("Категория расходов '$categoryName' уже существует")
                    }
                    else -> {
                        println("Ошибка базы данных: ${e.message}")
                    }
                }
                false
            }
        }

        fun addIncomeCategory(categoryName: String): Boolean {
            val sql = "INSERT INTO Income_categories (income_category_name) VALUES (?)"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setString(1, categoryName)

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("Категория доходов '$categoryName' успешно добавлена!")
                            true
                        } else {
                            println("Ошибка при добавлении категории доходов")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("UNIQUE constraint failed") == true -> {
                        println("Категория доходов '$categoryName' уже существует")
                    }
                    else -> {
                        println("Ошибка базы данных: ${e.message}")
                    }
                }
                false
            }
        }

        // Интерактивное добавление категории
        fun addCategoryInteractive() {
            println("=== ДОБАВЛЕНИЕ НОВОЙ КАТЕГОРИИ ===")

            print("Выберите тип категории (1 - расходы, 2 - доходы): ")
            val typeChoice = readLine()?.trim()

            when (typeChoice) {
                "1" -> {
                    print("Введите название категории расходов: ")
                    val name = readLine()?.trim()

                    if (!name.isNullOrEmpty()) {
                        addExpensesCategory(name)
                    } else {
                        println("Название категории не может быть пустым")
                    }
                }
                "2" -> {
                    print("Введите название категории доходов: ")
                    val name = readLine()?.trim()

                    if (!name.isNullOrEmpty()) {
                        addIncomeCategory(name)
                    } else {
                        println("Название категории не может быть пустым")
                    }
                }
                else -> println("Неверный выбор типа категории")
            }
        }
    }
}

// Функция main для тестирования
fun main() {
    AddCategory.addCategoryInteractive()
}