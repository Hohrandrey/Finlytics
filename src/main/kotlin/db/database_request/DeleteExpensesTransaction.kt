package db.database_request

import java.sql.DriverManager
import java.sql.SQLException

class DeleteExpensesTransaction {
    companion object {
        private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

        // Удаление транзакции расходов по ID
        fun deleteExpensesTransaction(transactionId: Int): Boolean {
            if (transactionId <= 0) {
                println("ID транзакции должен быть положительным числом")
                return false
            }

            val sql = "DELETE FROM expenses_transactions WHERE id_expenses_transaction = ?"

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.prepareStatement(sql).use { pstmt ->
                        pstmt.setInt(1, transactionId)

                        val affectedRows = pstmt.executeUpdate()

                        if (affectedRows > 0) {
                            println("Транзакция расходов с ID $transactionId успешно удалена!")
                            true
                        } else {
                            println("Транзакция расходов с ID $transactionId не найдена")
                            false
                        }
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица expenses_transactions не найдена")
                    }
                    else -> {
                        println("Ошибка базы данных при удалении транзакции расходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при удалении транзакции расходов: ${e.message}")
                false
            }
        }

        // Показать все транзакции расходов
        fun showExpensesTransactions(): Boolean {
            val sql = """
                SELECT et.id_expenses_transaction, et.expenses_transaction_name, 
                       et.expenses_transaction_sum, et.expenses_transaction_date,
                       ec.expenses_category_name
                FROM expenses_transactions et
                JOIN expenses_categories ec ON et.id_expenses_category = ec.id_expenses_category
                ORDER BY et.expenses_transaction_date DESC
            """.trimIndent()

            return try {
                DriverManager.getConnection(DB_URL).use { conn ->
                    conn.createStatement().use { stmt ->
                        val rs = stmt.executeQuery(sql)

                        println("\n=== ТРАНЗАКЦИИ РАСХОДОВ ===")
                        var hasTransactions = false

                        while (rs.next()) {
                            hasTransactions = true
                            val id = rs.getInt("id_expenses_transaction")
                            val name = rs.getString("expenses_transaction_name") ?: "Без названия"
                            val sum = rs.getDouble("expenses_transaction_sum")
                            val date = rs.getString("expenses_transaction_date")
                            val category = rs.getString("expenses_category_name")

                            println("ID: $id | Категория: $category | Сумма: $sum | Дата: $date | Название: $name")
                        }

                        if (!hasTransactions) {
                            println("Транзакции расходов не найдены")
                        }
                        println()
                        true
                    }
                }
            } catch (e: SQLException) {
                when {
                    e.message?.contains("no such table") == true -> {
                        println("Таблица expenses_transactions не найдена")
                    }
                    else -> {
                        println("Ошибка при получении транзакций расходов: ${e.message}")
                    }
                }
                false
            } catch (e: Exception) {
                println("Неожиданная ошибка при получении транзакций расходов: ${e.message}")
                false
            }
        }

        // Интерактивное удаление транзакции расходов
        fun deleteExpensesTransactionInteractive() {
            try {
                println("=== УДАЛЕНИЕ ТРАНЗАКЦИИ РАСХОДОВ ===")

                if (!showExpensesTransactions()) {
                    return
                }

                print("Введите ID транзакции расходов для удаления: ")
                val transactionId = readLine()?.trim()?.toIntOrNull()

                if (transactionId != null && transactionId > 0) {
                    deleteExpensesTransaction(transactionId)
                } else {
                    println("Неверный формат ID")
                }

            } catch (e: Exception) {
                println("Ошибка ввода: ${e.message}")
            }
        }
    }
}