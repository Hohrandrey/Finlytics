package db.database_request

import java.sql.DriverManager
import java.sql.SQLException

class DeleteIncomeTransaction {
    companion object {
        private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

        // Удаление транзакции доходов по ID
        fun deleteIncomeTransaction(transactionId: Int): Boolean {
            if (transactionId <= 0) {
                println("ID транзакции должен быть положительным числом")
                return false
            }

            val sql = "DELETE FROM Income_transactions WHERE id_income_transaction = ?"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setInt(1, transactionId)

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("✅ Транзакция доходов с ID $transactionId успешно удалена!")
                            true
                        } else {
                            println("❌ Транзакция доходов с ID $transactionId не найдена")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица Income_transactions не найдена")
                    }
                    else -> {
                        println("Ошибка базы данных при удалении транзакции доходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при удалении транзакции доходов: ${e.message}")
                false
            }
        }

        // Показать все транзакции доходов
        fun showIncomeTransactions(): Boolean {
            val sql = """
                SELECT it.id_income_transaction, it.income_transaction_name, 
                       it.income_transaction_sum, it.income_transaction_date,
                       ic.income_category_name
                FROM Income_transactions it
                JOIN Income_categories ic ON it.id_income_category = ic.id_income_category
                ORDER BY it.income_transaction_date DESC
            """.trimIndent()

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.createStatement().use { stmt ->
                        val rs = stmt.executeQuery(sql)

                        println("\n=== ТРАНЗАКЦИИ ДОХОДОВ ===")
                        var hasTransactions = false

                        while (rs.next()) {
                            hasTransactions = true
                            val id = rs.getInt("id_income_transaction")
                            val name = rs.getString("income_transaction_name") ?: "Без названия"
                            val sum = rs.getDouble("income_transaction_sum")
                            val date = rs.getString("income_transaction_date")
                            val category = rs.getString("income_category_name")

                            println("ID: $id | Категория: $category | Сумма: $sum | Дата: $date | Название: $name")
                        }

                        if (!hasTransactions) {
                            println("Транзакции доходов не найдены")
                        }
                        println()
                        true
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица Income_transactions не найдена")
                    }
                    else -> {
                        println("Ошибка при получении транзакций доходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при получении транзакций доходов: ${e.message}")
                false
            }
        }

        // Интерактивное удаление транзакции доходов
        fun deleteIncomeTransactionInteractive() {
            try {
                println("=== УДАЛЕНИЕ ТРАНЗАКЦИИ ДОХОДОВ ===")

                if (!showIncomeTransactions()) {
                    return
                }

                print("Введите ID транзакции доходов для удаления: ")
                val transactionId = readLine()?.trim()?.toIntOrNull()

                if (transactionId != null && transactionId > 0) {
                    deleteIncomeTransaction(transactionId)
                } else {
                    println("Неверный формат ID")
                }

            } catch (e: Exception) {
                println("Ошибка ввода: ${e.message}")
            }
        }
    }
}