package db.database_request

import java.sql.DriverManager

object AddExpensesTransaction {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

    fun addExpensesTransaction(name: String?, sum: Double, categoryId: Int, date: String): Boolean {
        if (sum <= 0 || categoryId <= 0) return false
        val sql = """
            INSERT INTO expenses_transactions 
            (expenses_transaction_name, expenses_transaction_sum, id_expenses_category, expenses_transaction_date) 
            VALUES (?, ?, ?, ?)
        """.trimIndent()
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, name)
                    pstmt.setDouble(2, sum)
                    pstmt.setInt(3, categoryId)
                    pstmt.setString(4, date)
                    pstmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}