package db.database_request

import java.sql.DriverManager

/**
 * Объект для управления категориями в базе данных.
 * Предоставляет функции добавления категорий доходов и расходов.
 */
object AddCategory {
    private val DB_URL = DatabaseConfig.DB_URL

    /**
     * Добавляет новую категорию доходов в базу данных.
     * Валидирует входные данные и логирует процесс выполнения.
     *
     * @param name Название категории доходов
     * @return true если категория успешно добавлена, false в случае ошибки
     */
    fun addIncomeCategory(name: String): Boolean {
        println("Добавление категории доходов: '$name'")
        if (name.isBlank()) {
            println("Имя категории пустое")
            return false
        }

        val sql = "INSERT INTO Income_categories (income_category_name) VALUES (?)"
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, name.trim())
                    val rowsAffected = pstmt.executeUpdate()
                    val result = rowsAffected > 0
                    println("Категория доходов '$name' добавлена. Затронуто строк: $rowsAffected, успех: $result")
                    result
                }
            }
        } catch (e: Exception) {
            println("Ошибка при добавлении категории доходов '$name': ${e.message}")
            e.printStackTrace()
            false
        }
    }

    /**
     * Добавляет новую категорию расходов в базу данных.
     * Валидирует входные данные и логирует процесс выполнения.
     *
     * @param name Название категории расходов
     * @return true если категория успешно добавлена, false в случае ошибки
     */
    fun addExpensesCategory(name: String): Boolean {
        println("Добавление категории расходов: '$name'")
        if (name.isBlank()) {
            println("Имя категории пустое")
            return false
        }

        val sql = "INSERT INTO expenses_categories (expenses_category_name) VALUES (?)"
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, name.trim())
                    val rowsAffected = pstmt.executeUpdate()
                    val result = rowsAffected > 0
                    println("Категория расходов '$name' добавлена. Затронуто строк: $rowsAffected, успех: $result")
                    result
                }
            }
        } catch (e: Exception) {
            println("Ошибка при добавлении категории расходов '$name': ${e.message}")
            e.printStackTrace()
            false
        }
    }
}
