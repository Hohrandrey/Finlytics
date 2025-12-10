package utils

import java.io.PrintStream
import java.nio.charset.StandardCharsets

/**
 * Утилита для настройки корректного логирования с поддержкой UTF-8.
 * Решает проблемы с отображением русских символов в консоли.
 */
object LoggingConfig {
    /**
     * Настраивает System.out и System.err для корректной работы с UTF-8.
     * Устанавливает системные свойства кодировки.
     */
    fun setupLogging() {
        // Устанавливаем UTF-8 кодировку для System.out и System.err
        System.setOut(PrintStream(System.out, true, StandardCharsets.UTF_8))
        System.setErr(PrintStream(System.err, true, StandardCharsets.UTF_8))

        // Устанавливаем системную кодировку
        System.setProperty("file.encoding", "UTF-8")
        System.setProperty("sun.stdout.encoding", "UTF-8")
        System.setProperty("sun.stderr.encoding", "UTF-8")

        println("Логирование настроено. Кодировка: ${System.getProperty("file.encoding")}")
    }
}
