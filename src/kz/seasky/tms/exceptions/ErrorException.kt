package kz.seasky.tms.exceptions

import io.ktor.http.*
import kz.seasky.tms.model.Response

/**
 * Handles all exceptions what we want to convert into [Response.Error]
 * afterwards omit it by StatusPage feature
 */
class ErrorException(
    override val message: String?,
    val statusCode: HttpStatusCode? = null,
    val withStackTrace: Boolean = false
) : Exception(message)