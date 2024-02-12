package dev.seano.restcountries.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawCountry(
	val name: String,
	@SerialName("m49_code") val m49Code: String,
	@SerialName("a2_code") val a2Code: String,
	@SerialName("a3_code") val a3Code: String,
	@SerialName("subregion_code") val subregionCode: String
)
