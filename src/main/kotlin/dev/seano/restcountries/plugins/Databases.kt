@file:Suppress("UnusedReceiverParameter")

package dev.seano.restcountries.plugins

import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
	val database = Database.connect(
		url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", user = "root", driver = "org.h2.Driver", password = ""
	)

	TransactionManager.defaultDatabase = database

	transaction {
		SchemaUtils.create(Countries, Regions, Flags)

		//#region Temporary data insertion
		val regions = listOf("Asia", "Africa", "South America", "North America", "Europe", "Oceania")
		Regions.batchInsert(regions) { this[Regions.name] = it }

		Countries.insert {
			it[name] = "United States of America"
			it[isoAlpha2] = "US"
			it[isoAlpha3] = "USA"
			it[isoNumeric] = "840"
			it[region] = 4
		}
		Flags.insert {
			it[countryCode] = "US"
			it[svg] = "https://flagcdn.com/us.svg"
			it[png] = "https://flagcdn.com/w320/us.png"
		}

		Countries.insert {
			it[name] = "Canada"
			it[isoAlpha2] = "CA"
			it[isoAlpha3] = "CAN"
			it[isoNumeric] = "124"
			it[region] = 4
		}
		Flags.insert {
			it[countryCode] = "CA"
			it[svg] = "https://flagcdn.com/ca.svg"
			it[png] = "https://flagcdn.com/w320/ca.png"
		}

		Countries.insert {
			it[name] = "United Kingdom of Great Britain and Northern Ireland"
			it[isoAlpha2] = "GB"
			it[isoAlpha3] = "GBR"
			it[isoNumeric] = "826"
			it[region] = 5
		}
		Flags.insert {
			it[countryCode] = "GB"
			it[svg] = "https://flagcdn.com/gb.svg"
			it[png] = "https://flagcdn.com/w320/gb.png"
		}

		Countries.insert {
			it[name] = "Japan"
			it[isoAlpha2] = "JP"
			it[isoAlpha3] = "JPN"
			it[isoNumeric] = "392"
			it[region] = 1
		}
		Flags.insert {
			it[countryCode] = "JP"
			it[svg] = "https://flagcdn.com/jp.svg"
			it[png] = "https://flagcdn.com/w320/jp.png"
		}
		//#endregion
	}
}

suspend fun <T> query(statement: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
	statement()
}

@Suppress("unused")
object Countries : IntIdTable("countries") {
	val name = varchar("name", 100).uniqueIndex()
	val isoAlpha2 = varchar("iso_alpha2", 2).uniqueIndex()
	val isoAlpha3 = varchar("iso_alpha3", 3).uniqueIndex()
	val isoNumeric = varchar("iso_numeric", 3).uniqueIndex()
	val region = reference("region", Regions)
}

object Regions : IntIdTable("regions") {
	val name = varchar("name", 100).uniqueIndex()
}

object Flags : IntIdTable("flags") {
	val countryCode = reference("country_code", Countries.isoAlpha2)
	val svg = varchar("svg", 27).uniqueIndex()
	val png = varchar("png", 32).uniqueIndex()
}
