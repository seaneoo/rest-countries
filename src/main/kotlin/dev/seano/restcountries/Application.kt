package dev.seano.restcountries

import dev.seano.restcountries.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
	configureSerialization()
	configureDatabases()
	configureMonitoring()
	configureHTTP()
	configureRouting()
}
