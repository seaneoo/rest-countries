package dev.seano.restcountries.models

import kotlinx.serialization.Serializable

@Serializable
data class Subregion(
	val code: String, val name: String
)
