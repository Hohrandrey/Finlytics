package db.database_request

import java.sql.DriverManager

object GetExpensesTransactions {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

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
            SELECT et.id_expenses_transaction, et.expenses_transaction_name, et.expenses_transaction_sum,
                   et.expenses_transaction_date, ec.expenses_category_name
            FROM expenses_transactions et
            JOIN expenses_categories ec ON et.id_expenses_category = ec.id_expenses_category
            ORDER BY et.expenses_transaction_date DESC
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