package db.database_request

import java.sql.DriverManager

/**
 * Объект для удаления транзакций расходов из базы данных.
 */
object DeleteExpensesTransaction {
    private val DB_URL = DatabaseConfig.DB_URL

    /**
     * Удаляет транзакцию расхода по её идентификатору.
     *
     * @param transactionId Идентификатор удаляемой транзакции (должен быть > 0)
     * @return true если транзакция успешно удалена, false в случае ошибки или некорректного ID
     */
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
