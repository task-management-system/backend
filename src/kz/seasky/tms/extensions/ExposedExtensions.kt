package kz.seasky.tms.extensions

import kz.seasky.tms.database.utils.Crypt
import kz.seasky.tms.model.paging.Paging
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.ResultSet

fun FieldSet.selectAll(column: Column<*>, paging: Paging): Query {
    return Query(this, null)
        .orderBy(column to paging.sortOrder)
        .limit(paging.limit, paging.offset)
}

fun <T> SizedIterable<T>.all(column: Column<*>, paging: Paging): SizedIterable<T> {
    return orderBy(column to paging.sortOrder)
        .limit(paging.limit, paging.offset)
}

/**
 * Raw SQL example:
 *  crypt('[password]', gen_salt('bf'))
 */
//TODO Rename
fun crypt(password: String) = CustomStringFunction(
    "crypt",
    stringLiteral(password),
    CustomStringFunction(
        "gen_salt",
        stringLiteral("bf")
    )
)


/**
 * Raw SQL example:
 *  expr for example equals "user"."password"
 *  expr = crypt('[password]', expr)
 */
//TODO Rename
fun Column<*>.crypt(password: String): Crypt = Crypt(password, this)

fun <T : Any> String.execAndMap(transform: (ResultSet) -> T): List<T> {
    val result = arrayListOf<T>()
    TransactionManager.current().exec(this) { rs ->
        while (rs.next()) {
            result += transform(rs)
        }
    }
    return result
}