package kz.seasky.tms.extensions

import kz.seasky.tms.model.paging.Paging
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.FieldSet
import org.jetbrains.exposed.sql.Query

fun FieldSet.selectAll(id: Column<Long>, paging: Paging): Query {
    return Query(this, null)
        .orderBy(id to paging.sortOrder)
        .limit(paging.limit, paging.offset)
}