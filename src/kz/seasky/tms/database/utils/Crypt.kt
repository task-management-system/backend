package kz.seasky.tms.database.utils

import org.jetbrains.exposed.sql.BooleanColumnType
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.QueryBuilder

//TODO Rename
class Crypt(
    private val password: String,
    private val expr: Expression<*>,
) : Function<Boolean>(BooleanColumnType()) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder): Unit = queryBuilder {
        +expr
        +""" = crypt("""
        +"'$password',"
        +expr
        +")"
    }
}