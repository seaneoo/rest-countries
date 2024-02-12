@file:Suppress("UnusedReceiverParameter")

package dev.seano.restcountries.plugins

import dev.seano.restcountries.DatabaseSingleton
import dev.seano.restcountries.data.dsl.Regions
import dev.seano.restcountries.data.dsl.Subregions
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
		SchemaUtils.create(Regions, Subregions)

		insertData()
	}
}

private fun insertData() {
	val regionsFile = object {}.javaClass.getResourceAsStream("/regions.json")?.bufferedReader()?.readText()
		?: throw Exception("Could not read from file \"regions.json\".")
	val regionsData = Json.decodeFromString<List<RawRegion>>(regionsFile)

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
	}
}

suspend fun <T> query(statement: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
	statement()
}
