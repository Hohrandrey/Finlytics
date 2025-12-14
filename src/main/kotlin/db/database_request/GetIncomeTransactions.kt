package db.database_request

import java.sql.DriverManager

/**
 * Объект для получения транзакций доходов из базы данных.
 * Выполняет JOIN с таблицей категорий для получения полной информации.
 */
object GetIncomeTransactions {
    private val DB_URL = DatabaseConfig.DB_URL

    /**
     * Модель строки результата запроса транзакций доходов.
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
     * Получает все транзакции доходов из базы данных.
     * Транзакции возвращаются в порядке убывания даты (от новых к старым).
     *
     * @return Список всех транзакций доходов с информацией о категориях
     */
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

    /**
     * Получает максимальный идентификатор транзакции дохода.
     * Используется для получения ID только что добавленной транзакции.
     *
     * @return Максимальный ID или 0, если транзакций нет
     */
    fun getLastId() = getAll().maxOfOrNull { it.id } ?: 0
}
