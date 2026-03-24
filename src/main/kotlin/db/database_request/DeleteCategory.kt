package db.database_request

import java.sql.DriverManager

/**
 * Объект для удаления категорий из базы данных.
 * Поддерживает удаление как категорий доходов, так и категорий расходов.
 *
 * Важно: удаление категории возможно только если с ней не связано ни одной операции.
 * Ограничение ON DELETE RESTRICT в схеме БД предотвращает удаление категорий,
 * связанных с существующими транзакциями.
 *
 * @author Finlytics Team
 * @since 1.0.0
 */
object DeleteCategory {
    private val DB_URL = DatabaseConfig.DB_URL

    /**
     * Удаляет категорию доходов по её идентификатору.
     *
     * @param categoryId Идентификатор удаляемой категории (должен быть > 0)
     * @return true если категория успешно удалена, false в случае ошибки или некорректного ID
     */
    fun deleteIncomeCategory(categoryId: Int): Boolean {
        if (categoryId <= 0) return false
        val sql = "DELETE FROM Income_categories WHERE id_income_category = ?"
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setInt(1, categoryId)
                    val rowsAffected = pstmt.executeUpdate()
                    println("Удалена категория доходов с ID $categoryId. Затронуто строк: $rowsAffected")
                    rowsAffected > 0
                }
            }
        } catch (e: Exception) {
            println("Ошибка при удалении категории доходов: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    /**
     * Удаляет категорию расходов по её идентификатору.
     *
     * @param categoryId Идентификатор удаляемой категории (должен быть > 0)
     * @return true если категория успешно удалена, false в случае ошибки или некорректного ID
     */
    fun deleteExpensesCategory(categoryId: Int): Boolean {
        if (categoryId <= 0) return false
        val sql = "DELETE FROM expenses_categories WHERE id_expenses_category = ?"
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setInt(1, categoryId)
                    val rowsAffected = pstmt.executeUpdate()
                    println("Удалена категория расходов с ID $categoryId. Затронуто строк: $rowsAffected")
                    rowsAffected > 0
                }
            }
        } catch (e: Exception) {
            println("Ошибка при удалении категории расходов: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}