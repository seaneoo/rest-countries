@file:Suppress("SpellCheckingInspection", "PropertyName")

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
val prometeus_version: String by project
val mariadb_version: String by project

plugins {
	kotlin("jvm") version "1.9.22"
	id("io.ktor.plugin") version "2.3.8"
	id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "dev.seano"
version = "0.0.1"

application {
	mainClass.set("dev.seano.restcountries.ApplicationKt")

	val isDevelopment: Boolean = project.ext.has("development")
	applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("io.ktor:ktor-server-core-jvm")
	implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
	implementation("io.ktor:ktor-server-content-negotiation-jvm")
	implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
	implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
	implementation("com.h2database:h2:$h2_version")
	implementation("io.ktor:ktor-server-metrics-micrometer-jvm")
	implementation("io.micrometer:micrometer-registry-prometheus:$prometeus_version")
	implementation("io.ktor:ktor-server-call-logging-jvm")
	implementation("io.ktor:ktor-server-call-id-jvm")
	implementation("io.ktor:ktor-server-conditional-headers-jvm")
	implementation("io.ktor:ktor-server-cors-jvm")
	implementation("io.ktor:ktor-server-host-common-jvm")
	implementation("io.ktor:ktor-server-status-pages-jvm")
	implementation("io.ktor:ktor-server-netty-jvm")
	implementation("ch.qos.logback:logback-classic:$logback_version")
	implementation("org.mariadb.jdbc:mariadb-java-client:$mariadb_version")

	testImplementation("io.ktor:ktor-server-tests-jvm")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
