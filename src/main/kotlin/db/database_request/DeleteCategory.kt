package db.database_request

import java.sql.DriverManager
import java.sql.SQLException

class DeleteCategory {
    companion object {
        private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

        // Удаление категории расходов по ID
        fun deleteExpensesCategory(categoryId: Int): Boolean {
            if (categoryId <= 0) {
                println("ID категории должен быть положительным числом")
                return false
            }

            val sql = "DELETE FROM expenses_categories WHERE id_expenses_category = ?"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setInt(1, categoryId)

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("✅ Категория расходов с ID $categoryId успешно удалена!")
                            true
                        } else {
                            println("❌ Категория расходов с ID $categoryId не найдена")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица expenses_categories не найдена. Проверьте структуру базы данных.")
                    }
                    e.message?.contains("FOREIGN KEY constraint failed") == true -> {
                        println("Нельзя удалить категорию расходов с ID $categoryId, так как она используется в других таблицах")
                    }
                    else -> {
                        println("Ошибка базы данных при удалении категории расходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при удалении категории расходов: ${e.message}")
                false
            }
        }

        // Удаление категории расходов по названию
        fun deleteExpensesCategoryByName(categoryName: String): Boolean {
            if (categoryName.isBlank()) {
                println("Название категории расходов не может быть пустым")
                return false
            }

            val sql = "DELETE FROM expenses_categories WHERE expenses_category_name = ?"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setString(1, categoryName.trim())

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("✅ Категория расходов '$categoryName' успешно удалена!")
                            true
                        } else {
                            println("❌ Категория расходов '$categoryName' не найдена")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица expenses_categories не найдена. Проверьте структуру базы данных.")
                    }
                    e.message?.contains("FOREIGN KEY constraint failed") == true -> {
                        println("Нельзя удалить категорию расходов '$categoryName', так как она используется в других таблицах")
                    }
                    else -> {
                        println("Ошибка базы данных при удалении категории расходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при удалении категории расходов: ${e.message}")
                false
            }
        }

        // Удаление категории доходов по ID
        fun deleteIncomeCategory(categoryId: Int): Boolean {
            if (categoryId <= 0) {
                println("ID категории должен быть положительным числом")
                return false
            }

            val sql = "DELETE FROM Income_categories WHERE id_income_category = ?"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setInt(1, categoryId)

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("✅ Категория доходов с ID $categoryId успешно удалена!")
                            true
                        } else {
                            println("❌ Категория доходов с ID $categoryId не найдена")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица Income_categories не найдена. Проверьте структуру базы данных.")
                    }
                    e.message?.contains("FOREIGN KEY constraint failed") == true -> {
                        println("Нельзя удалить категорию доходов с ID $categoryId, так как она используется в других таблицах")
                    }
                    else -> {
                        println("Ошибка базы данных при удалении категории доходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при удалении категории доходов: ${e.message}")
                false
            }
        }

        // Удаление категории доходов по названию
        fun deleteIncomeCategoryByName(categoryName: String): Boolean {
            if (categoryName.isBlank()) {
                println("Название категории доходов не может быть пустым")
                return false
            }

            val sql = "DELETE FROM Income_categories WHERE income_category_name = ?"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setString(1, categoryName.trim())

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("✅ Категория доходов '$categoryName' успешно удалена!")
                            true
                        } else {
                            println("❌ Категория доходов '$categoryName' не найдена")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица Income_categories не найдена. Проверьте структуру базы данных.")
                    }
                    e.message?.contains("FOREIGN KEY constraint failed") == true -> {
                        println("Нельзя удалить категорию доходов '$categoryName', так как она используется в других таблицах")
                    }
                    else -> {
                        println("Ошибка базы данных при удалении категории доходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при удалении категории доходов: ${e.message}")
                false
            }
        }

        // Показать все категории расходов
        fun showExpensesCategories(): Boolean {
            val sql = "SELECT id_expenses_category, expenses_category_name FROM expenses_categories ORDER BY expenses_category_name"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.createStatement().use { stmt ->
                        val rs = stmt.executeQuery(sql)

                        println("\n=== КАТЕГОРИИ РАСХОДОВ ===")
                        var hasCategories = false

                        while (rs.next()) {
                            hasCategories = true
                            val id = rs.getInt("id_expenses_category")
                            val name = rs.getString("expenses_category_name")

                            println("ID: $id | Название: $name")
                        }

                        if (!hasCategories) {
                            println("Категории расходов не найдены")
                        }
                        println()
                        true
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица expenses_categories не найдена. Проверьте структуру базы данных.")
                    }
                    else -> {
                        println("Ошибка при получении категорий расходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при получении категорий расходов: ${e.message}")
                false
            }
        }

        // Показать все категории доходов
        fun showIncomeCategories(): Boolean {
            val sql = "SELECT id_income_category, income_category_name FROM Income_categories ORDER BY income_category_name"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.createStatement().use { stmt ->
                        val rs = stmt.executeQuery(sql)

                        println("\n=== КАТЕГОРИИ ДОХОДОВ ===")
                        var hasCategories = false

                        while (rs.next()) {
                            hasCategories = true
                            val id = rs.getInt("id_income_category")
                            val name = rs.getString("income_category_name")

                            println("ID: $id | Название: $name")
                        }

                        if (!hasCategories) {
                            println("Категории доходов не найдены")
                        }
                        println()
                        true
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица Income_categories не найдена. Проверьте структуру базы данных.")
                    }
                    else -> {
                        println("Ошибка при получении категорий доходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при получении категорий доходов: ${e.message}")
                false
            }
        }

        // Показать все категории
        fun showAllCategories(): Boolean {
            val success1 = showExpensesCategories()
            val success2 = showIncomeCategories()
            return success1 && success2
        }

        // Интерактивное удаление категории
        fun deleteCategoryInteractive() {
            try {
                println("=== УДАЛЕНИЕ КАТЕГОРИИ ===")

                print("Выберите тип категории (1 - расходы, 2 - доходы): ")
                val typeChoice = readLine()?.trim()

                when (typeChoice) {
                    "1" -> {
                        if (showExpensesCategories()) {
                            print("Выберите способ удаления (1 - по ID, 2 - по названию): ")
                            val methodChoice = readLine()?.trim()

                            when (methodChoice) {
                                "1" -> {
                                    print("Введите ID категории расходов для удаления: ")
                                    val idInput = readLine()?.trim()?.toIntOrNull()

                                    if (idInput != null && idInput > 0) {
                                        deleteExpensesCategory(idInput)
                                    } else {
                                        println("Неверный формат ID")
                                    }
                                }
                                "2" -> {
                                    print("Введите название категории расходов для удаления: ")
                                    val name = readLine()?.trim()

                                    if (!name.isNullOrEmpty()) {
                                        deleteExpensesCategoryByName(name)
                                    } else {
                                        println("Название категории не может быть пустым")
                                    }
                                }
                                else -> println("Неверный выбор метода")
                            }
                        }
                    }
                    "2" -> {
                        if (showIncomeCategories()) {
                            print("Выберите способ удаления (1 - по ID, 2 - по названию): ")
                            val methodChoice = readLine()?.trim()

                            when (methodChoice) {
                                "1" -> {
                                    print("Введите ID категории доходов для удаления: ")
                                    val idInput = readLine()?.trim()?.toIntOrNull()

                                    if (idInput != null && idInput > 0) {
                                        deleteIncomeCategory(idInput)
                                    } else {
                                        println("Неверный формат ID")
                                    }
                                }
                                "2" -> {
                                    print("Введите название категории доходов для удаления: ")
                                    val name = readLine()?.trim()

                                    if (!name.isNullOrEmpty()) {
                                        deleteIncomeCategoryByName(name)
                                    } else {
                                        println("Название категории не может быть пустым")
                                    }
                                }
                                else -> println("Неверный выбор метода")
                            }
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
