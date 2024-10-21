plugins {
    kotlin("jvm") version "1.8.10"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test")) // Dependency for testing
    implementation("org.json:json:20220924")
}

tasks.test {
    useJUnitPlatform() // Устанавливаем JUnit для тестирования
}
kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}