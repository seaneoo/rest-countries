package dev.seano.restcountries.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RawRegion(
	val code: String, val name: String, val subregions: List<RawSubregion>
)
