package kz.seasky.tms.extensions

import io.ktor.http.*
import kotlinx.uuid.UUID
import kotlinx.uuid.UUIDExperimentalAPI
import kz.seasky.tms.model.paging.Paging
import org.joda.time.DateTime

fun Parameters.asPaging(): Paging {
    return Paging(
        page = get("page")?.toLong(),
        size = get("size")?.toInt(),
        order = get("order")
    )
}

fun String.isValidTime(): Boolean {
//    val dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")

    return try {
        DateTime(this)
//        dtf.parseDateTime(this)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

@UUIDExperimentalAPI
fun String.isValidUUID(): Boolean {
    return UUID.isValidUUIDString(this)
}