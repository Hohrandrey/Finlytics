package db.database_request

import java.sql.DriverManager

object DeleteExpensesTransaction {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

    fun deleteExpensesTransaction(transactionId: Int): Boolean {
        if (transactionId <= 0) return false

        val sql = "DELETE FROM expenses_transactions WHERE id_expenses_transaction = ?"

        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setInt(1, transactionId)
                    pstmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}