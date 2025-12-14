package db.database_request

import java.sql.DriverManager

/**
 * Объект для обновления транзакций доходов в базе данных.
 */
object UpdateIncomeTransaction {
    private val DB_URL = DatabaseConfig.DB_URL

    /**
     * Обновляет существующую транзакцию дохода.
     *
     * @param transactionId Идентификатор обновляемой транзакции (должен быть > 0)
     * @param name Новое название транзакции (может быть null)
     * @param sum Новая сумма транзакции (должна быть положительной)
     * @param categoryName Новое название категории (должна существовать в базе)
     * @param date Новая дата транзакции в формате строки (ГГГГ-ММ-ДД)
     * @return true если транзакция успешно обновлена, false в случае ошибки или некорректных данных
     */
    fun update(
        transactionId: Int,
        name: String? = null,
        sum: Double,
        categoryName: String,
        date: String
    ): Boolean {
        val categoryId = GetIncomeCategories.getIdByName(categoryName) ?: return false
        if (transactionId <= 0 || sum <= 0 || categoryId <= 0) return false

        val sql = """
            UPDATE Income_transactions 
            SET income_transaction_name = ?, 
                income_transaction_sum = ?, 
                id_income_category = ?, 
                income_transaction_date = ? 
            WHERE id_income_transaction = ?
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
