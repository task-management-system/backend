package kz.seasky.tms.extensions

import io.ktor.http.*
import kz.seasky.tms.model.paging.Paging

fun Parameters.asPaging(): Paging {
    return Paging(
        page = get("page")?.toLong(),
        size = get("size")?.toInt(),
        order = get("order")
    )
}