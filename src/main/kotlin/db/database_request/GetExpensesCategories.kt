package db.database_request

import java.sql.DriverManager

object GetExpensesCategories {
    private val DB_URL = DatabaseConfig.DB_URL

    fun getAll(): List<Pair<Int, String>> {
        val list = mutableListOf<Pair<Int, String>>()
        val sql = "SELECT id_expenses_category, expenses_category_name FROM expenses_categories ORDER BY expenses_category_name"
        try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.createStatement().use { stmt ->
                    val rs = stmt.executeQuery(sql)
                    while (rs.next()) {
                        list.add(rs.getInt(1) to rs.getString(2))
                    }
                    println("Загружено категорий расходов: ${list.size}")
                }
            }
        } catch (e: Exception) {
            println("Ошибка при получении категорий расходов: ${e.message}")
        }
        return list
    }

    fun getAllNames() = getAll().map { it.second }
    fun getIdByName(name: String) = getAll().find { it.second == name }?.first
}
