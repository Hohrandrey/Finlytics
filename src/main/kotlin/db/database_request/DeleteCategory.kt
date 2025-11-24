package db.database_request

import java.sql.DriverManager

object DeleteCategory {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

    fun deleteIncomeCategory(categoryId: Int): Boolean {
        if (categoryId <= 0) return false
        val sql = "DELETE FROM Income_categories WHERE id_income_category = ?"
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setInt(1, categoryId)
                    pstmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            false  // Если FOREIGN KEY — вернёт false (нельзя удалить)
        }
    }

    fun deleteExpensesCategory(categoryId: Int): Boolean {
        if (categoryId <= 0) return false
        val sql = "DELETE FROM expenses_categories WHERE id_expenses_category = ?"
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setInt(1, categoryId)
                    pstmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}