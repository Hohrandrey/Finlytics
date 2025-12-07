package utils

import java.io.PrintStream
import java.nio.charset.StandardCharsets

object LoggingConfig {
    fun setupLogging() {
        System.setOut(PrintStream(System.out, true, StandardCharsets.UTF_8))
        System.setErr(PrintStream(System.err, true, StandardCharsets.UTF_8))

        System.setProperty("file.encoding", "UTF-8")
        System.setProperty("sun.stdout.encoding", "UTF-8")
        System.setProperty("sun.stderr.encoding", "UTF-8")

        println("Логирование настроено. Кодировка: ${System.getProperty("file.encoding")}")
    }
}
