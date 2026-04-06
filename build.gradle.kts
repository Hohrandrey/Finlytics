import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

plugins {
    kotlin("jvm") version "1.9.24"
    id("org.jetbrains.compose") version "1.6.11"
    id("org.jetbrains.dokka") version "1.9.20"
    // Плагин application НЕ нужен, так как compose.desktop уже предоставляет задачи запуска
}

group = "com.finlytics"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvmToolchain(21)
}

// Настройка компиляции Kotlin
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "21"
    kotlinOptions.freeCompilerArgs += listOf("-opt-in=kotlin.RequiresOptIn")
}

// Настройка кодировки
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}

tasks.withType<JavaExec> {
    systemProperty("file.encoding", "UTF-8")
}

dependencies {
    // Основное приложение
    implementation(compose.desktop.currentOs)
    implementation("org.xerial:sqlite-jdbc:3.46.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")

    // Тестирование
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}

// Настройка задач тестирования
tasks.test {
    useJUnitPlatform()
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
    outputs.upToDateWhen { false }
}

// Задача для генерации документации (Javadoc)
tasks.register<Javadoc>("generateJavadoc") {
    group = "documentation"
    description = "Generates Javadoc documentation"

    val sourceSet = sourceSets.main.get()
    source = sourceSet.allJava
    classpath = sourceSet.compileClasspath
    destinationDir = file("${layout.buildDirectory.get().asFile}/docs/javadoc")

    doFirst {
        (options as CoreJavadocOptions).apply {
            encoding = "UTF-8"
            memberLevel = JavadocMemberLevel.PROTECTED
            windowTitle = "Finlytics API Documentation"
            addStringOption("Xdoclint:none", "-quiet")
        }
    }

    exclude("**/ui/theme/icons/**")
    dependsOn("classes")
}

// === ИСПРАВЛЕННАЯ ЗАДАЧА FAT JAR ===
// Мы не используем плагин 'application', чтобы избежать конфликта задач 'run'
tasks.register<Jar>("fatJar") {
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        // Указываем главный класс вашего приложения
        attributes["Main-Class"] = "main.MainKt"
    }

    // Собираем все зависимости runtime и классы проекта в один jar
    from({
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    })

    // Добавляем скомпилированные классы основного исходного набора
    with(tasks.jar.get())
}

// Настройка Compose Desktop приложения
compose.desktop {
    application {
        mainClass = "main.MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb
            )
            packageName = "Finlytics"
            packageVersion = "1.0.0"

            windows {
                menu = true
                console = true
            }

            linux {
                packageName = "finlytics"
            }
            macOS {
                bundleID = "com.finlytics.app"
            }
        }
    }
}