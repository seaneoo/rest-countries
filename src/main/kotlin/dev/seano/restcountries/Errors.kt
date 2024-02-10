package dev.seano.restcountries

import io.ktor.http.*

open class HTTPException(
	val statusCode: HttpStatusCode = HttpStatusCode.InternalServerError, message: String? = statusCode.description
) : Exception(message)

class NotFoundException(message: String? = "Resource not found") : HTTPException(HttpStatusCode.NotFound, message)

class TooManyRequestsException(message: String? = "Too many requests") :
	HTTPException(HttpStatusCode.TooManyRequests, message)

class BadRequestException(message: String? = "Bad request") : HTTPException(HttpStatusCode.BadRequest, message)
