package db.database_request

import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddIncomeTransaction {
    companion object {
        private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

        // Проверка существования категории доходов
        private fun checkIncomeCategoryExists(categoryId: Int): Boolean {
            val sql = "SELECT 1 FROM Income_categories WHERE id_income_category = ?"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setInt(1, categoryId)
                        val rs = pstmt.executeQuery()
                        rs.next() // возвращает true если категория найдена
                    }
                }
            } catch (e: Exception) {
                false
            }
        }

        // Добавление транзакции доходов
        fun addIncomeTransaction(
            name: String?,
            sum: Double,
            categoryId: Int,
            date: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        ): Boolean {
            if (sum <= 0) {
                println("Сумма транзакции должна быть положительной")
                return false
            }

            if (categoryId <= 0) {
                println("ID категории должен быть положительным числом")
                return false
            }

            // Проверяем существование категории перед добавлением
            if (!checkIncomeCategoryExists(categoryId)) {
                println("Категория доходов с ID $categoryId не найдена. Транзакция не добавлена.")
                return false
            }

            val sql = """
                INSERT INTO Income_transactions 
                (income_transaction_name, income_transaction_sum, id_income_category, income_transaction_date) 
                VALUES (?, ?, ?, ?)
            """.trimIndent()

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setString(1, name)
                        pstmt.setDouble(2, sum)
                        pstmt.setInt(3, categoryId)
                        pstmt.setString(4, date)

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("Транзакция доходов успешно добавлена!")
                            true
                        } else {
                            println("Ошибка при добавлении транзакции доходов")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("FOREIGN KEY constraint failed") == true -> {
                        println("Категория с ID $categoryId не найдена. Транзакция не добавлена.")
                    }
                    e.message?.contains("no such table") == true -> {
                        println("Таблица Income_transactions не найдена")
                    }
                    else -> {
                        println("Ошибка базы данных при добавлении транзакции доходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при добавлении транзакции доходов: ${e.message}")
                false
            }
        }

        // Интерактивное добавление транзакции доходов
        fun addIncomeTransactionInteractive() {
            try {
                println("=== ДОБАВЛЕНИЕ ТРАНЗАКЦИИ ДОХОДОВ ===")

                // Показываем доступные категории
                showIncomeCategories()

                print("Введите ID категории доходов: ")
                val categoryId = readLine()?.trim()?.toIntOrNull()

                if (categoryId == null || categoryId <= 0) {
                    println("Неверный ID категории")
                    return
                }

                // Проверяем существование категории перед запросом суммы
                if (!checkIncomeCategoryExists(categoryId)) {
                    println("Категория доходов с ID $categoryId не найдена")
                    return
                }

                print("Введите сумму дохода: ")
                val sum = readLine()?.trim()?.toDoubleOrNull()

                if (sum == null || sum <= 0) {
                    println("Неверная сумма")
                    return
                }

                print("Введите название транзакции (необязательно): ")
                val name = readLine()?.trim().takeIf { !it.isNullOrEmpty() }

                print("Введите дату (гггг-мм-дд, по умолчанию сегодня): ")
                val dateInput = readLine()?.trim()
                val date = if (!dateInput.isNullOrEmpty()) dateInput else
                    LocalDate.now().format(DateTimeFormatter.ISO_DATE)

                addIncomeTransaction(name, sum, categoryId, date)

            } catch (e: Exception) {
                println("Ошибка ввода: ${e.message}")
            }
        }

        private fun showIncomeCategories() {
            val sql = "SELECT id_income_category, income_category_name FROM Income_categories ORDER BY income_category_name"

            try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.createStatement().use { stmt ->
                        val rs = stmt.executeQuery(sql)

                        println("\nДоступные категории доходов:")
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
            } catch (e: Exception) {
                println("Ошибка при загрузке категорий: ${e.message}")
            }
        }
    }
}