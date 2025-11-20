package db.database_request

import java.sql.DriverManager
import java.sql.SQLException

class AddCategory {
    companion object {
        private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

        // Добавление категории расходов
        fun addExpensesCategory(categoryName: String): Boolean {
            if (categoryName.isBlank()) {
                println("Название категории расходов не может быть пустым")
                return false
            }

            val sql = "INSERT INTO expenses_categories (expenses_category_name) VALUES (?)"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setString(1, categoryName.trim())

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("✅ Категория расходов '$categoryName' успешно добавлена!")
                            true
                        } else {
                            println("❌ Ошибка при добавлении категории расходов")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("UNIQUE constraint failed") == true -> {
                        println("Категория расходов '$categoryName' уже существует")
                    }
                    e.message?.contains("no such table") == true -> {
                        println("Таблица expenses_categories не найдена. Проверьте структуру базы данных.")
                    }
                    else -> {
                        println("Ошибка базы данных при добавлении категории расходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при добавлении категории расходов: ${e.message}")
                false
            }
        }

        // Добавление категории доходов
        fun addIncomeCategory(categoryName: String): Boolean {
            if (categoryName.isBlank()) {
                println("Название категории доходов не может быть пустым")
                return false
            }

            val sql = "INSERT INTO Income_categories (income_category_name) VALUES (?)"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setString(1, categoryName.trim())

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("✅ Категория доходов '$categoryName' успешно добавлена!")
                            true
                        } else {
                            println("❌ Ошибка при добавлении категории доходов")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("UNIQUE constraint failed") == true -> {
                        println("Категория доходов '$categoryName' уже существует")
                    }
                    e.message?.contains("no such table") == true -> {
                        println("Таблица Income_categories не найдена. Проверьте структуру базы данных.")
                    }
                    else -> {
                        println("Ошибка базы данных при добавлении категории доходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при добавлении категории доходов: ${e.message}")
                false
            }
        }

        // Интерактивное добавление категории
        fun addCategoryInteractive() {
            try {
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
            } catch (e: Exception) {
                println("Ошибка ввода: ${e.message}")
            }
        }
    }
}
