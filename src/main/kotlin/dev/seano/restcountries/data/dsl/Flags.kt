package dev.seano.restcountries.data.dsl

import org.jetbrains.exposed.sql.Table

object Flags : Table("flags") {
	val countryCode = reference("country_a2_code", Countries.a2Code).uniqueIndex()
	val png = varchar("flag_png", 40).nullable()
	val svg = varchar("flag_svg", 40).nullable()
	val emoji = varchar("flag_emoji", 10).nullable()

	override val primaryKey = PrimaryKey(countryCode)
}
