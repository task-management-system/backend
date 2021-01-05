package kz.tms.utils

import io.ktor.application.*
import io.ktor.response.*
import kz.tms.model.response.Message
import kz.tms.model.response.Response
import java.util.*

suspend fun <T> ApplicationCall.respond(
    message: Message? = null,
    data: T
) {
    respond(Response(message, data))
}

fun setAndReturnDate(field: Int = Calendar.SECOND, amount: Int = 0): Date {
    val calendar = Calendar.getInstance()
    calendar.set(field, amount)
    return calendar.time
}