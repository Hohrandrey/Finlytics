package db.database_request

import java.sql.DriverManager

/**
 * Объект для добавления новых категорий в базу данных.
 * Поддерживает добавление как категорий доходов, так и категорий расходов.
 *
 * @author Finlytics Team
 * @since 1.0.0
 */
object AddCategory {
    private val DB_URL = DatabaseConfig.DB_URL

    /**
     * Добавляет новую категорию доходов в таблицу Income_categories.
     *
     * @param name Название категории (не должно быть пустым)
     * @return true если категория успешно добавлена, false в случае ошибки или пустого имени
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
     * Добавляет новую категорию расходов в таблицу expenses_categories.
     *
     * @param name Название категории (не должно быть пустым)
     * @return true если категория успешно добавлена, false в случае ошибки или пустого имени
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