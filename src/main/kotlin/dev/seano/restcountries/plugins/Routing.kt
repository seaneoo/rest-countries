package dev.seano.restcountries.plugins

import dev.seano.restcountries.HTTPException
import dev.seano.restcountries.routes.countryRoutes
import dev.seano.restcountries.routes.regionRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	install(StatusPages) {
		exception<Throwable> { call, cause ->
			val statusCode = HttpStatusCode.InternalServerError
			call.respondText(text = "${statusCode.value} $cause", status = statusCode)
		}

		exception<NotFoundException> { call, cause ->
			val statusCode = HttpStatusCode.NotFound
			call.respondText(text = "${statusCode.value} $cause", status = statusCode)
		}

		exception<HTTPException> { call, cause ->
			call.respondText(text = "${cause.statusCode.value} ${cause.message}", status = cause.statusCode)
		}
	}

	routing {
		get("/") {
			call.respond(HttpStatusCode.OK.description)
		}

		regionRoutes()
		countryRoutes()
	}
}
