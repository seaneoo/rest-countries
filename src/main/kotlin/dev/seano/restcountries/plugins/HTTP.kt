package dev.seano.restcountries.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
	install(ConditionalHeaders)

	install(CORS) {
		anyHost()
	}
}
