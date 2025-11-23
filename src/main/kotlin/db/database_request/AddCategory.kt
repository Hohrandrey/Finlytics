package db.database_request

import java.sql.DriverManager

object AddCategory {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

    fun addIncomeCategory(name: String): Boolean {
        if (name.isBlank()) return false
        val sql = "INSERT INTO Income_categories (income_category_name) VALUES (?)"
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, name.trim())
                    pstmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            false
        }
    }

    fun addExpensesCategory(name: String): Boolean {
        if (name.isBlank()) return false
        val sql = "INSERT INTO expenses_categories (expenses_category_name) VALUES (?)"
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, name.trim())
                    pstmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}