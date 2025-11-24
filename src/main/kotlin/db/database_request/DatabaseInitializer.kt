package db.database_request

import java.sql.DriverManager

object DatabaseInitializer {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/db/database/Finlytics.db"

    fun createTablesIfNotExist() {
        val sql = """
            CREATE TABLE IF NOT EXISTS Income_categories (
                id_income_category INTEGER PRIMARY KEY AUTOINCREMENT,
                income_category_name TEXT NOT NULL UNIQUE
            );
            CREATE TABLE IF NOT EXISTS expenses_categories (
                id_expenses_category INTEGER PRIMARY KEY AUTOINCREMENT,
                expenses_category_name TEXT NOT NULL UNIQUE
            );
            CREATE TABLE IF NOT EXISTS Income_transactions (
                id_income_transaction INTEGER PRIMARY KEY AUTOINCREMENT,
                income_transaction_name TEXT,
                income_transaction_sum REAL NOT NULL,
                id_income_category INTEGER,
                income_transaction_date TEXT NOT NULL,
                FOREIGN KEY (id_income_category) REFERENCES Income_categories(id_income_category) ON DELETE RESTRICT
            );
            CREATE TABLE IF NOT EXISTS expenses_transactions (
                id_expenses_transaction INTEGER PRIMARY KEY AUTOINCREMENT,
                expenses_transaction_name TEXT,
                expenses_transaction_sum REAL NOT NULL,
                id_expenses_category INTEGER,
                expenses_transaction_date TEXT NOT NULL,
                FOREIGN KEY (id_expenses_category) REFERENCES expenses_categories(id_expenses_category) ON DELETE RESTRICT
            );
        """.trimIndent()

        try {
            DriverManager.getConnection(DB_URL).use { conn ->
                conn.createStatement().use { stmt ->
                    sql.split(";").forEach { query ->
                        if (query.trim().isNotEmpty()) stmt.execute(query.trim())
                    }
                }
            }
        } catch (e: Exception) {
            println("Ошибка инициализации БД: ${e.message}")
        }
    }
}