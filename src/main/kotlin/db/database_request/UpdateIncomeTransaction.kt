package db.database_request

import java.sql.DriverManager

object UpdateIncomeTransaction {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

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