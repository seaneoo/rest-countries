@file:Suppress("UnusedReceiverParameter")

package dev.seano.restcountries.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
	val database = Database.connect(
		url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", user = "root", driver = "org.h2.Driver", password = ""
	)

	TransactionManager.defaultDatabase = database

	transaction {
		SchemaUtils.create(Countries, Regions)
	}
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
