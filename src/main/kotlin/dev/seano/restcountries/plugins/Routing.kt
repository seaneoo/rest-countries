package dev.seano.restcountries.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	install(StatusPages) {
		exception<Throwable> { call, cause ->
			val statusCode = HttpStatusCode.InternalServerError
			call.respondText(text = "${statusCode.value} $cause", status = statusCode)
		}
	}

	routing {

	}
}
