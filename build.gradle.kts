plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "com.yourcompany"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.yourcompany.MainKt")
}

tasks.test {
    useJUnitPlatform()
}