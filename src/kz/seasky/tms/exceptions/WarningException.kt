package kz.seasky.tms.exceptions

import io.ktor.http.*
import kz.seasky.tms.model.Response

/**
 * Handles all exceptions what we want to convert into [Response.Error]
 * afterwards omit it by StatusPage feature
 */
class WarningException(
    override val message: String?,
    val statusCode: HttpStatusCode? = null
) : Exception(message)