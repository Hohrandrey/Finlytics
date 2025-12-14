package db.database_request

import java.sql.DriverManager

object DeleteCategory {
    private val DB_URL = DatabaseConfig.DB_URL

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
