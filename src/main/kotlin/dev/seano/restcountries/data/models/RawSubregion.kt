package dev.seano.restcountries.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RawSubregion(
	val code: String, val name: String
)
