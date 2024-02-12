@file:Suppress("UnusedReceiverParameter")

package dev.seano.restcountries.plugins

import dev.seano.restcountries.DatabaseSingleton
import dev.seano.restcountries.data.dsl.Countries
import dev.seano.restcountries.data.dsl.Flags
import dev.seano.restcountries.data.dsl.Regions
import dev.seano.restcountries.data.dsl.Subregions
import dev.seano.restcountries.data.models.RawCountry
import dev.seano.restcountries.data.models.RawRegion
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
	val embedded = environment.config.property("ktor.environment").getString().equals("dev", true)
	val database = DatabaseSingleton.getInstance(environment.config).connect(embedded)

	TransactionManager.defaultDatabase = database

	transaction {
		SchemaUtils.create(Regions, Subregions, Countries, Flags)

		insertData()
	}
}

fun getFlagEmoji(countryCode: String): String {
	val codePoints = countryCode.uppercase().map { 127397 + it.code }.toIntArray()
	return String(codePoints, 0, codePoints.size)
}

private fun insertData() {
	val regionsFile = object {}.javaClass.getResourceAsStream("/regions.json")?.bufferedReader()?.readText()
		?: throw Exception("Could not read from file \"regions.json\".")
	val regionsData = Json.decodeFromString<List<RawRegion>>(regionsFile)

	val countriesFile = object {}.javaClass.getResourceAsStream("/countries.json")?.bufferedReader()?.readText()
		?: throw Exception("Could not read from file \"countries.json\".")
	val countriesData = Json.decodeFromString<List<RawCountry>>(countriesFile)

	transaction {
		Regions.batchInsert(regionsData) { rawRegion ->
			this[Regions.code] = rawRegion.code
			this[Regions.name] = rawRegion.name
		}

		regionsData.forEach { rawRegion ->
			Subregions.batchInsert(rawRegion.subregions) { rawSubregion ->
				this[Subregions.code] = rawSubregion.code
				this[Subregions.name] = rawSubregion.name
				this[Subregions.regionCode] = rawRegion.code
			}
		}

		Countries.batchInsert(countriesData) { rawCountry ->
			this[Countries.a2Code] = rawCountry.a2Code
			this[Countries.a3Code] = rawCountry.a3Code
			this[Countries.m49Code] = rawCountry.m49Code
			this[Countries.name] = rawCountry.name
			if (rawCountry.subregionCode.isNotBlank()) this[Countries.subregionCode] = rawCountry.subregionCode
		}

		Flags.batchInsert(countriesData) { rawCountry ->
			this[Flags.countryCode] = rawCountry.a2Code
			this[Flags.png] = "https://flagcdn.com/w320/${rawCountry.a2Code.lowercase()}.png"
			this[Flags.svg] = "https://flagcdn.com/${rawCountry.a2Code.lowercase()}.svg"
			this[Flags.emoji] = getFlagEmoji(rawCountry.a2Code)
		}
	}
}

suspend fun <T> query(statement: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
	statement()
}
