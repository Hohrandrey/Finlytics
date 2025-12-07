package db.database_request

import java.io.File

object DatabaseConfig {
    private const val DB_NAME = "Finlytics.db"

    val DB_URL: String by lazy {
        val possiblePaths = listOf(
            File(System.getProperty("user.dir"), DB_NAME).absolutePath,
            File("src/main/kotlin/db/database", DB_NAME).absolutePath,
            File("db/database", DB_NAME).absolutePath,
            DB_NAME
        )

        var dbPath = possiblePaths.firstOrNull { File(it).exists() } ?: possiblePaths[0]

        val dbFile = File(dbPath)
        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            try {
                dbFile.createNewFile()
                println("Создана новая база данных: ${dbFile.absolutePath}")
            } catch (e: Exception) {
                println("Ошибка при создании базы данных: ${e.message}")
                dbPath = possiblePaths[1]
                val altFile = File(dbPath)
                altFile.parentFile?.mkdirs()
                altFile.createNewFile()
                println("Создана альтернативная база данных: ${altFile.absolutePath}")
            }
        }

        println("Используется база данных: ${dbFile.absolutePath}")
        "jdbc:sqlite:$dbPath"
    }
}
