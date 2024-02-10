package dev.seano.restcountries

import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DatabaseSingleton private constructor(private val applicationConfig: ApplicationConfig) {
	companion object {
		private var instance: DatabaseSingleton? = null
		val logger: Logger = LoggerFactory.getLogger(DatabaseSingleton::class.java.simpleName)

		fun getInstance(applicationConfig: ApplicationConfig): DatabaseSingleton = instance ?: synchronized(this) {
			instance ?: DatabaseSingleton(applicationConfig).also {
				instance = it
			}
		}
	}

	private var initialized = false
	private var database: Database? = null

	/**
	 * Connect to the database server.
	 *
	 * @param embedded True if the embedded database should be used rather than the external MariaDB server.
	 * @return [Database] The database instance.
	 */
	fun connect(embedded: Boolean): Database? {
		logger.info("Attempting to connect to the database server.")

		if (initialized) {
			logger.warn("The database server is already connected using ${database?.vendor} ${database?.version}.")
			return null
		}

		if (embedded) {
			initialized = true
			database = Database.connect(
				"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver", user = "root", password = ""
			)
		} else {
			try {
				val driver = "org.mariadb.jdbc.Driver"
				Class.forName(driver)

				val host = applicationConfig.property("database.host").getString()
				val port = applicationConfig.property("database.port").getString()
				val user = applicationConfig.property("database.user").getString()
				val pass = applicationConfig.property("database.pass").getString()
				val name = applicationConfig.property("database.name").getString()

				database = Database.connect("jdbc:mariadb://$host:$port/$name", driver, user, password = pass)
			} catch (e: Exception) {
				database = null
			}
		}

		return if (database == null) {
			logger.error("The connection to the database server failed.")
			null
		} else {
			logger.info("The connection to the database server was successful using ${database?.vendor} ${database?.version}.")
			initialized = true
			database
		}
	}
}
