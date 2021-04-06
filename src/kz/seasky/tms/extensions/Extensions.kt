package kz.seasky.tms.extensions

import io.ktor.http.*
import kotlinx.uuid.UUID
import kotlinx.uuid.UUIDExperimentalAPI
import kz.seasky.tms.model.paging.Paging
import org.joda.time.DateTime

fun Parameters.asPaging(): Paging {
    return Paging(
        page = get("page")?.toLongOrNull(),
        size = get("size")?.toIntOrNull(),
        order = get("order")
    )
}

@Suppress("LiftReturnOrAssignment")
fun String.isValidTime(): Boolean = try {
    DateTime(this)
    true
} catch (e: IllegalArgumentException) {
    false
}

/**
 * @return true if UUID is valid, false otherwise
 */
@OptIn(UUIDExperimentalAPI::class)
fun String.isValidUUID(): Boolean {
    return UUID.isValidUUIDString(this)
}

fun DateTime.plain(): String {
    //@formatter:off
    return  year().asString +
            monthOfYear().asString +
            dayOfMonth().asString +
            hourOfDay().asString +
            minuteOfHour().asString +
            secondOfMinute().asString +
            zone.getOffset(this)
    //@formatter:on
}