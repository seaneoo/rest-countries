package dev.seano.restcountries.data.dsl

import org.jetbrains.exposed.sql.Table

object Regions : Table("regions") {
	val code = varchar("region_code", 3).uniqueIndex()
	val name = varchar("region_name", 40).uniqueIndex()

	override val primaryKey = PrimaryKey(code)
}
