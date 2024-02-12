package dev.seano.restcountries.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
	val name: String,
	@SerialName("m49_code") val m49Code: String,
	@SerialName("a2_code") val a2Code: String,
	@SerialName("a3_code") val a3Code: String,
	@SerialName("subregion") val subregionCode: String?,
	@SerialName("region") val regionCode: String?,
	val flags: Flag
)
