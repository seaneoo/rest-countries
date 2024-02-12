package dev.seano.restcountries.routes

import dev.seano.restcountries.BadRequestException
import dev.seano.restcountries.NotFoundException
import dev.seano.restcountries.data.dsl.Countries
import dev.seano.restcountries.models.Country
import dev.seano.restcountries.plugins.query
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.selectAll

fun Route.countryRoutes() {
	route("/countries") {
		get {
			query {
				val countries =
					Countries.selectAll().orderBy(Countries.a2Code).groupBy { it[Countries.a2Code] }.map { (_, rows) ->
						val countryRow = rows.first()
						Country(
							countryRow[Countries.name],
							countryRow[Countries.m49Code],
							countryRow[Countries.a2Code],
							countryRow[Countries.a3Code]
						)
					}

				call.respond(countries)
			}
		}

		get("/{countryCode}") {
			val countryCode = call.parameters["countryCode"]
			if (countryCode.isNullOrBlank()) throw BadRequestException()

			query {
				val country = Countries.selectAll().where { Countries.a2Code.lowerCase() eq countryCode.lowercase() }
					.groupBy { it[Countries.a2Code] }.map { (_, rows) ->
						val countryRow = rows.first()
						Country(
							countryRow[Countries.name],
							countryRow[Countries.m49Code],
							countryRow[Countries.a2Code],
							countryRow[Countries.a3Code]
						)
					}

				if (country.isEmpty()) throw NotFoundException()

				call.respond(country)
			}
		}
	}
}
