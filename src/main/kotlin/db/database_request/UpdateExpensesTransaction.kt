package db.database_request

import java.sql.DriverManager

object UpdateExpensesTransaction {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

    fun update(
        transactionId: Int,
        name: String? = null,
        sum: Double,
        categoryName: String,
        date: String
    ): Boolean {
        val categoryId = GetExpensesCategories.getIdByName(categoryName) ?: return false
        if (transactionId <= 0 || sum <= 0 || categoryId <= 0) return false

        val sql = """
            UPDATE expenses_transactions 
            SET expenses_transaction_name = ?, 
                expenses_transaction_sum = ?, 
                id_expenses_category = ?, 
                expenses_transaction_date = ? 
            WHERE id_expenses_transaction = ?
        """.trimIndent()

        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, name)
                    pstmt.setDouble(2, sum)
                    pstmt.setInt(3, categoryId)
                    pstmt.setString(4, date)
                    pstmt.setInt(5, transactionId)
                    pstmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}