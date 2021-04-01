package kz.seasky.tms.database.utils

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager

fun <T : Table> T.upsert(
    vararg keys: Column<*>,
    body: T.(InsertStatement<Number>) -> Unit
) = UpsertStatement<Number>(this, keys = keys).apply {
    body(this)
    execute(TransactionManager.current())
}

class UpsertStatement<Key : Any>(
    table: Table,
    isIgnore: Boolean = false,
    private vararg val keys: Column<*>
) : InsertStatement<Key>(table, isIgnore) {
    override fun prepareSQL(transaction: Transaction) = buildString {
        append(super.prepareSQL(transaction))
        append(transaction.onUpdateSql(values.keys, *keys))
    }
}

private fun Transaction.onUpdateSql(values: Iterable<Column<*>>, vararg keys: Column<*>) = buildString {
    if (db.vendor == "postgresql") {
        append(" ON CONFLICT (${keys.joinToString(",") { identity(it) }})")
        values.filter { it !in keys }.takeIf { it.isNotEmpty() }?.let { fields ->
            append(" DO UPDATE SET ")
            fields.joinTo(this, ", ") { "${identity(it)} = EXCLUDED.${identity(it)}" }
        } ?: append(" DO NOTHING")
    } else {
        append(" ON DUPLICATE KEY UPDATE ")
        values.joinTo(this, ", ") { "${identity(it)} = VALUES(${identity(it)})" }
    }
}