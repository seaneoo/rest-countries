package dev.seano.restcountries.routes

import dev.seano.restcountries.BadRequestException
import dev.seano.restcountries.NotFoundException
import dev.seano.restcountries.data.dsl.Regions
import dev.seano.restcountries.data.dsl.Subregions
import dev.seano.restcountries.models.Region
import dev.seano.restcountries.models.Subregion
import dev.seano.restcountries.plugins.query
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll

fun Route.regionRoutes() {
	route("/regions") {
		get {
			query {
				val regions =
					(Regions leftJoin Subregions).selectAll().orderBy(Regions.code).groupBy { it[Regions.code] }
						.map { (_, rows) ->
							val regionRow = rows.first()
							val subregions = rows.map { Subregion(it[Subregions.code], it[Subregions.name]) }
							Region(regionRow[Regions.code], regionRow[Regions.name], subregions)
						}

				call.respond(regions)
			}
		}

		get("/{regionCode}") {
			val regionCode = call.parameters["regionCode"]
			if (regionCode.isNullOrBlank()) throw BadRequestException()

			query {
				val region = (Regions leftJoin Subregions).selectAll().where { Regions.code eq regionCode }
					.groupBy { it[Regions.code] }.map { (_, rows) ->
						val regionRow = rows.first()
						val subregions = rows.map { Subregion(it[Subregions.code], it[Subregions.name]) }
						Region(regionRow[Regions.code], regionRow[Regions.name], subregions)
					}

				if (region.isEmpty()) throw NotFoundException()

				call.respond(region)
			}
		}
	}
}
