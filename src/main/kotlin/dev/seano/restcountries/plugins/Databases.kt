@file:Suppress("UnusedReceiverParameter")

package dev.seano.restcountries.plugins

import dev.seano.restcountries.DatabaseSingleton
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
	val embedded = environment.config.property("ktor.environment").getString().equals("dev", true)
	val database = DatabaseSingleton.getInstance(environment.config).connect(embedded)

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
			it[region] = "North America"
		}
		Flags.insert {
			it[svg] = "https://flagcdn.com/us.svg"
			it[png] = "https://flagcdn.com/w320/us.png"
			it[country] = "US"
		}

		Countries.insert {
			it[name] = "Canada"
			it[isoAlpha2] = "CA"
			it[isoAlpha3] = "CAN"
			it[isoNumeric] = "124"
			it[region] = "North America"
		}
		Flags.insert {
			it[svg] = "https://flagcdn.com/ca.svg"
			it[png] = "https://flagcdn.com/w320/ca.png"
			it[country] = "CA"
		}

		Countries.insert {
			it[name] = "United Kingdom of Great Britain and Northern Ireland"
			it[isoAlpha2] = "GB"
			it[isoAlpha3] = "GBR"
			it[isoNumeric] = "826"
			it[region] = "Europe"
		}
		Flags.insert {
			it[svg] = "https://flagcdn.com/gb.svg"
			it[png] = "https://flagcdn.com/w320/gb.png"
			it[country] = "GB"
		}

		Countries.insert {
			it[name] = "Japan"
			it[isoAlpha2] = "JP"
			it[isoAlpha3] = "JPN"
			it[isoNumeric] = "392"
			it[region] = "Asia"
		}
		Flags.insert {
			it[svg] = "https://flagcdn.com/jp.svg"
			it[png] = "https://flagcdn.com/w320/jp.png"
			it[country] = "JP"
		}
		//#endregion
	}
}

suspend fun <T> query(statement: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
	statement()
}

object Countries : Table("countries") {
	val id = integer("id").autoIncrement()
	val name = varchar("name", 100).uniqueIndex()
	val isoAlpha2 = varchar("iso_alpha2", 2).uniqueIndex()
	val isoAlpha3 = varchar("iso_alpha3", 3).uniqueIndex()
	val isoNumeric = varchar("iso_numeric", 3).uniqueIndex()
	val region = reference("region", Regions.name)

	override val primaryKey = PrimaryKey(id)
}

object Regions : Table("regions") {
	val id = integer("id").autoIncrement()
	val name = varchar("name", 100).uniqueIndex()

	override val primaryKey = PrimaryKey(id)
}

object Flags : Table("flags") {
	private val id = integer("id").autoIncrement()
	val svg = varchar("svg", 27).uniqueIndex()
	val png = varchar("png", 32).uniqueIndex()
	val country = reference("country", Countries.isoAlpha2)

	override val primaryKey = PrimaryKey(id)
}
