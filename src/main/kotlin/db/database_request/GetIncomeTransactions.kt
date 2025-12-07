package db.database_request

import java.sql.DriverManager

object GetIncomeTransactions {
    private val DB_URL = DatabaseConfig.DB_URL

    data class Row(
        val id: Int,
        val name: String?,
        val amount: Double,
        val date: String,
        val category: String
    )

    fun getAll(): List<Row> {
        val list = mutableListOf<Row>()
        val sql = """
            SELECT it.id_income_transaction, it.income_transaction_name, it.income_transaction_sum,
                   it.income_transaction_date, ic.income_category_name
            FROM Income_transactions it
            JOIN Income_categories ic ON it.id_income_category = ic.id_income_category
            ORDER BY it.income_transaction_date DESC
        """.trimIndent()
        try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.createStatement().use { stmt ->
                    val rs = stmt.executeQuery(sql)
                    while (rs.next()) {
                        list.add(Row(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5)))
                    }
                }
            }
        } catch (e: Exception) { }
        return list
    }

    fun getLastId() = getAll().maxOfOrNull { it.id } ?: 0
}