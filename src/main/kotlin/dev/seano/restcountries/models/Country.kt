package dev.seano.restcountries.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
	val id: Int,
	val name: String,
	@SerialName("iso_alpha2") val isoAlpha2: String,
	@SerialName("iso_alpha3") val isoAlpha3: String,
	@SerialName("iso_numeric") val isoNumeric: String,
	val region: String,
	val flags: Flag
)
