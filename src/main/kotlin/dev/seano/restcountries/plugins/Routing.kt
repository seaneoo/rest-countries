package dev.seano.restcountries.plugins

import dev.seano.restcountries.HTTPException
import dev.seano.restcountries.models.Country
import dev.seano.restcountries.models.Flag
import dev.seano.restcountries.models.Region
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll

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

		//#region Temporary routing
		get("/regions") {
			query {
				val regions = Regions.selectAll().sortedBy { it[Regions.id] }.map {
					Region(it[Regions.id], it[Regions.name])
				}

				call.respond(regions)
			}
		}

		get("/countries") {
			query {
				val countries = (Countries innerJoin Flags).selectAll().sortedBy { it[Countries.id] }.map {
					val flag = Flag(it[Flags.svg], it[Flags.png])
					Country(
						it[Countries.id],
						it[Countries.name],
						it[Countries.isoAlpha2],
						it[Countries.isoAlpha3],
						it[Countries.isoNumeric],
						it[Countries.region],
						flag
					)
				}

				call.respond(countries)
			}
		}
		//#endregion
	}
}
