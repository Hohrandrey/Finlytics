package db.database_request

import java.sql.DriverManager

object GetExpensesCategories {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

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
                }
            }
        } catch (e: Exception) { }
        return list
    }

    fun getAllNames() = getAll().map { it.second }
    fun getIdByName(name: String) = getAll().find { it.second == name }?.first
}