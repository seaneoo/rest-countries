package dev.seano.restcountries.models

import kotlinx.serialization.Serializable

@Serializable
data class Region(
	val id: Int, val name: String
)
