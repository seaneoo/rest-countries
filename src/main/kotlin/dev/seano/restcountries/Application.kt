package dev.seano.restcountries

import dev.seano.restcountries.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
	val env = applicationEngineEnvironment { envConfig() }
	embeddedServer(Netty, env).start(wait = true)
}

fun Application.module() {
	configureSerialization()
	configureDatabases()
	configureMonitoring()
	configureHTTP()
	configureRouting()
}

fun ApplicationEngineEnvironmentBuilder.envConfig() {
	module {
		module()
	}
	connector {
		host = "0.0.0.0"
		port = 8080
	}
}
