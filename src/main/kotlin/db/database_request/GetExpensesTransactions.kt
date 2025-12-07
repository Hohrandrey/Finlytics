package db.database_request

import java.sql.DriverManager

/**
 * Объект для получения транзакций расходов из базы данных.
 * Выполняет JOIN с таблицей категорий для получения полной информации.
 */
object GetExpensesTransactions {
    private val DB_URL = DatabaseConfig.DB_URL

    /**
     * Модель строки результата запроса транзакций расходов.
     *
     * @property id Уникальный идентификатор транзакции
     * @property name Название транзакции (может быть null)
     * @property amount Сумма транзакции
     * @property date Дата транзакции в формате строки
     * @property category Название категории транзакции
     */
    data class Row(
        val id: Int,
        val name: String?,
        val amount: Double,
        val date: String,
        val category: String
    )

    /**
     * Получает все транзакции расходов из базы данных.
     * Транзакции возвращаются в порядке убывания даты (от новых к старым).
     *
     * @return Список всех транзакций расходов с информацией о категориях
     */
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

    /**
     * Получает максимальный идентификатор транзакции расхода.
     * Используется для получения ID только что добавленной транзакции.
     *
     * @return Максимальный ID или 0, если транзакций нет
     */
    fun getLastId() = getAll().maxOfOrNull { it.id } ?: 0
}
