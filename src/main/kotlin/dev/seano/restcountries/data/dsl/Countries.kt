package dev.seano.restcountries.data.dsl

import org.jetbrains.exposed.sql.Table

object Countries : Table("countries") {
	val a2Code = varchar("country_a2_code", 2).uniqueIndex()
	val a3Code = varchar("country_a3_code", 3).uniqueIndex()
	val m49Code = varchar("country_m49_code", 3).uniqueIndex()
	val name = varchar("country_name", 100).uniqueIndex()

	override val primaryKey = PrimaryKey(a2Code)
}
