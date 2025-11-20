package db.database_request

import java.sql.DriverManager
import java.sql.SQLException

class DeleteCategory {
    companion object {
        private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

        // Удаление категории расходов по ID
        fun deleteExpensesCategory(categoryId: Int): Boolean {
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
                println("❌ Ошибка базы данных: ${e.message}")
                false
            }
        }

        // Удаление категории расходов по названию
        fun deleteExpensesCategoryByName(categoryName: String): Boolean {
            val sql = "DELETE FROM expenses_categories WHERE expenses_category_name = ?"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setString(1, categoryName)

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
                println("❌ Ошибка базы данных: ${e.message}")
                false
            }
        }

        // Удаление категории доходов по ID
        fun deleteIncomeCategory(categoryId: Int): Boolean {
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
                println("❌ Ошибка базы данных: ${e.message}")
                false
            }
        }

        // Удаление категории доходов по названию
        fun deleteIncomeCategoryByName(categoryName: String): Boolean {
            val sql = "DELETE FROM Income_categories WHERE income_category_name = ?"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setString(1, categoryName)

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
                println("❌ Ошибка базы данных: ${e.message}")
                false
            }
        }

        // Показать все категории расходов
        fun showExpensesCategories() {
            val sql = "SELECT id_expenses_category, expenses_category_name FROM expenses_categories ORDER BY expenses_category_name"

            try {
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
                    }
                }
            } catch (e: SQLException) {
                println("❌ Ошибка при получении категорий расходов: ${e.message}")
            }
        }

        // Показать все категории доходов
        fun showIncomeCategories() {
            val sql = "SELECT id_income_category, income_category_name FROM Income_categories ORDER BY income_category_name"

            try {
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
                    }
                }
            } catch (e: SQLException) {
                println("❌ Ошибка при получении категорий доходов: ${e.message}")
            }
        }

        // Показать все категории
        fun showAllCategories() {
            showExpensesCategories()
            showIncomeCategories()
        }

        // Интерактивное удаление категории
        fun deleteCategoryInteractive() {
            println("=== УДАЛЕНИЕ КАТЕГОРИИ ===")

            print("Выберите тип категории (1 - расходы, 2 - доходы): ")
            val typeChoice = readLine()?.trim()

            when (typeChoice) {
                "1" -> {
                    showExpensesCategories()
                    print("Выберите способ удаления (1 - по ID, 2 - по названию): ")
                    val methodChoice = readLine()?.trim()

                    when (methodChoice) {
                        "1" -> {
                            print("Введите ID категории расходов для удаления: ")
                            val idInput = readLine()?.trim()?.toIntOrNull()

                            if (idInput != null) {
                                deleteExpensesCategory(idInput)
                            } else {
                                println("❌ Неверный формат ID")
                            }
                        }
                        "2" -> {
                            print("Введите название категории расходов для удаления: ")
                            val name = readLine()?.trim()

                            if (!name.isNullOrEmpty()) {
                                deleteExpensesCategoryByName(name)
                            } else {
                                println("❌ Название категории не может быть пустым")
                            }
                        }
                        else -> println("❌ Неверный выбор метода")
                    }
                }
                "2" -> {
                    showIncomeCategories()
                    print("Выберите способ удаления (1 - по ID, 2 - по названию): ")
                    val methodChoice = readLine()?.trim()

                    when (methodChoice) {
                        "1" -> {
                            print("Введите ID категории доходов для удаления: ")
                            val idInput = readLine()?.trim()?.toIntOrNull()

                            if (idInput != null) {
                                deleteIncomeCategory(idInput)
                            } else {
                                println("❌ Неверный формат ID")
                            }
                        }
                        "2" -> {
                            print("Введите название категории доходов для удаления: ")
                            val name = readLine()?.trim()

                            if (!name.isNullOrEmpty()) {
                                deleteIncomeCategoryByName(name)
                            } else {
                                println("❌ Название категории не может быть пустым")
                            }
                        }
                        else -> println("❌ Неверный выбор метода")
                    }
                }
                else -> println("❌ Неверный выбор типа категории")
            }
        }
    }
}

// Функция main для тестирования удаления
fun main() {
}