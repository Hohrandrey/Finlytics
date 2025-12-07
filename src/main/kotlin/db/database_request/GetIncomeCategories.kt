package db.database_request

import java.sql.DriverManager

/**
 * Объект для получения категорий доходов из базы данных.
 */
object GetIncomeCategories {
    private val DB_URL = DatabaseConfig.DB_URL

    /**
     * Получает все категории доходов из базы данных.
     * Категории возвращаются отсортированными по названию.
     *
     * @return Список пар (id категории, название категории)
     */
    fun getAll(): List<Pair<Int, String>> {
        val list = mutableListOf<Pair<Int, String>>()
        val sql = "SELECT id_income_category, income_category_name FROM Income_categories ORDER BY income_category_name"
        try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.createStatement().use { stmt ->
                    val rs = stmt.executeQuery(sql)
                    while (rs.next()) {
                        list.add(rs.getInt(1) to rs.getString(2))
                    }
                    println("Загружено категорий доходов: ${list.size}")
                }
            }
        } catch (e: Exception) {
            println("Ошибка при получении категорий доходов: ${e.message}")
        }
        return list
    }

    /**
     * Получает список названий всех категорий доходов.
     *
     * @return Список названий категорий
     */
    fun getAllNames() = getAll().map { it.second }

    /**
     * Получает идентификатор категории доходов по её названию.
     *
     * @param name Название категории
     * @return Идентификатор категории или null, если категория не найдена
     */
    fun getIdByName(name: String) = getAll().find { it.second == name }?.first
}
