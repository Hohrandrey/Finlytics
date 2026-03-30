plugins {
    kotlin("jvm") version "1.9.24"
    id("org.jetbrains.compose") version "1.6.11"
    id("org.jetbrains.dokka") version "1.9.20"
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "21"
    kotlinOptions.freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
}

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
    implementation(compose.desktop.currentOs)
    implementation("org.xerial:sqlite-jdbc:3.46.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")
}

// Простая задача для Javadoc
tasks.register<Javadoc>("generateJavadoc") {
    group = "documentation"
    description = "Generates Javadoc documentation"

    source = sourceSets.main.get().allJava
    classpath = sourceSets.main.get().compileClasspath
    destinationDir = file("${layout.buildDirectory.get().asFile}/docs/javadoc")

    // Настройки через стандартный механизм
    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        memberLevel = JavadocMemberLevel.PROTECTED
        windowTitle = "Finlytics API Documentation"
    }

    exclude("**/ui/theme/icons/**")
    dependsOn("classes")
}

compose.desktop {
    application {
        mainClass = "main.MainKt"

        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
            packageName = "Finlytics"
            packageVersion = "1.0.0"

            windows {
                menu = true
                console = true
            }
        }
    }
}