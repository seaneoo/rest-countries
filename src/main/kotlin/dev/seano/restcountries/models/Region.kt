package dev.seano.restcountries.models

import kotlinx.serialization.Serializable

@Serializable
data class Region(
	val code: String, val name: String, val subregions: List<Subregion>
)
