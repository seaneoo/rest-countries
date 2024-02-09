package dev.seano.restcountries.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

fun Application.configureDatabases() {
	val database = Database.connect(
		url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", user = "root", driver = "org.h2.Driver", password = ""
	)

	TransactionManager.defaultDatabase = database
}
