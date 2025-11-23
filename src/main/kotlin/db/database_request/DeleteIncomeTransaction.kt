package db.database_request

import java.sql.DriverManager

object DeleteIncomeTransaction {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

    fun deleteIncomeTransaction(transactionId: Int): Boolean {
        if (transactionId <= 0) return false

        val sql = "DELETE FROM Income_transactions WHERE id_income_transaction = ?"

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