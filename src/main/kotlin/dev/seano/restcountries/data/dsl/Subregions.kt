package dev.seano.restcountries.data.dsl

import org.jetbrains.exposed.sql.Table

@Suppress("unused")
object Subregions : Table("subregions") {
	val code = varchar("subregion_code", 3).uniqueIndex()
	val name = varchar("subregion_name", 40).uniqueIndex()
	val regionCode = reference("region_code", Regions.code)

	override val primaryKey = PrimaryKey(code)
}
