val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project
val h2_version: String by project
val exposed_version: String by project
val commons_codec_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("com.google.cloud.tools.jib") version "3.2.1"
}

// Read version from version.txt
val versionFile = file("version.txt")
val currentVersion = versionFile.readText().trim()

// Split the version into major, minor, and patch
val versionParts = currentVersion.split(".")
val majorVersion = versionParts[0].toInt()
val minorVersion = versionParts[1].toInt()
val patchVersion = versionParts[2].toInt()

// Increment the patch version
val newPatchVersion = patchVersion + 1

// Construct the new version string
val newVersion = "$majorVersion.$minorVersion.$newPatchVersion"

group = "example.com"
version = newVersion

// Write the new version back to version.txt
versionFile.writeText(newVersion)

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}
ktor {
    fatJar {
        archiveFileName.set("rfm.ktor-server.jar")
    }
    docker {
        jreVersion.set(JavaVersion.VERSION_19)

    }
    jib {
        from {
            image = "openjdk:19-jdk-alpine"
        }
        to {
            image = "rfm9300/ktor-central"
            tags = setOf("${project.version}")
        }
    }
}


dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("org.flywaydb:flyway-core:9.20.1")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    implementation("commons-codec:commons-codec:$commons_codec_version")

    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")

    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")

    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2") // Adjust the version if necessary


    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

