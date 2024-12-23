plugins {
    kotlin("jvm") version "2.0.21"
}

group = "br.com.lukinhasssss"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awssdk:secretsmanager:2.29.39")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")

    implementation("com.aallam.openai:openai-client:4.0.0-beta01")
    implementation("io.ktor:ktor-client-core:3.0.2")
    implementation("io.ktor:ktor-client-okhttp:3.0.2")

    implementation("com.aallam.ktoken:ktoken:0.4.0")

    testImplementation(kotlin("test"))
}

// configurations.all {
//     resolutionStrategy {
//         failOnVersionConflict()
//     }
// }

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}