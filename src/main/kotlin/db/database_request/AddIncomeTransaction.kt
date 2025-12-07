package db.database_request

import java.sql.DriverManager

/**
 * Объект для добавления транзакций доходов в базу данных.
 * Реализует операцию INSERT для таблицы Income_transactions.
 */
object AddIncomeTransaction {
    private val DB_URL = DatabaseConfig.DB_URL

    /**
     * Добавляет новую транзакцию дохода в базу данных.
     *
     * @param name Название транзакции (может быть null)
     * @param sum Сумма транзакции (должна быть положительной)
     * @param categoryId Идентификатор категории дохода (должен быть > 0)
     * @param date Дата транзакции в формате строки (ГГГГ-ММ-ДД)
     * @return true если транзакция успешно добавлена, false в случае ошибки или некорректных данных
     */
    fun addIncomeTransaction(name: String?, sum: Double, categoryId: Int, date: String): Boolean {
        if (sum <= 0 || categoryId <= 0) return false
        val sql = """
            INSERT INTO Income_transactions 
            (income_transaction_name, income_transaction_sum, id_income_category, income_transaction_date) 
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
