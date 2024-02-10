package dev.seano.restcountries.models

import kotlinx.serialization.Serializable

@Serializable
data class Flag(val png: String, val svg: String)
