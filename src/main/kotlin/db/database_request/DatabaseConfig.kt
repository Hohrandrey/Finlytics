package db.database_request

import java.io.File

/**
 * Конфигурация подключения к базе данных SQLite.
 * Автоматически находит или создает файл базы данных в доступных локациях.
 *
 * Основные функции:
 * - Определение пути к файлу БД с поиском в нескольких возможных локациях
 * - Автоматическое создание файла БД при его отсутствии
 * - Формирование JDBC URL для подключения к SQLite
 *
 * @author Finlytics Team
 * @since 1.0.0
 */
object DatabaseConfig {
    private const val DB_NAME = "Finlytics.db"

    /**
     * URL подключения к базе данных SQLite.
     * Ищет существующий файл БД или создает новый в текущей директории.
     *
     * Поиск выполняется в следующих путях:
     * 1. Текущая директория приложения (user.dir)
     * 2. Путь для разработки (src/main/kotlin/db/database)
     * 3. Альтернативный путь (db/database)
     * 4. Просто имя файла
     *
     * @return JDBC URL для подключения к SQLite
     */
    val DB_URL: String by lazy {
        // Пробуем несколько путей для поиска базы данных
        val possiblePaths = listOf(
            File(System.getProperty("user.dir"), DB_NAME).absolutePath, // Текущая директория
            File("src/main/kotlin/db/database", DB_NAME).absolutePath,  // Путь при разработке
            File("db/database", DB_NAME).absolutePath,                  // Альтернативный путь
            DB_NAME                                                                    // Просто имя файла
        )

        var dbPath = possiblePaths.firstOrNull { File(it).exists() } ?: possiblePaths[0]

        // Если файл не существует, создадим его в текущей директории
        val dbFile = File(dbPath)
        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            try {
                dbFile.createNewFile()
                println("Создана новая база данных: ${dbFile.absolutePath}")
            } catch (e: Exception) {
                println("Ошибка при создании базы данных: ${e.message}")
                // Если не удалось создать по этому пути, пробуем другой
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