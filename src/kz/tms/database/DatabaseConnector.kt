package kz.tms.database

import kotlinx.coroutines.runBlocking
import kz.tms.database.data.claims.ClaimsTable
import kz.tms.database.data.roles.RolesTable
import kz.tms.database.data.user.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransaction
import javax.sql.DataSource

class DatabaseConnector(dataSource: DataSource) {
    private val database = Database.connect(dataSource)

    private val tables = arrayOf(
        UsersTable,
        RolesTable,
        ClaimsTable
    )

    //TODO Разобрать копипастную магию
    init {
        runBlocking {
            newTransaction {
                SchemaUtils.create(*tables)
            }
        }
    }

    suspend fun deleteAllTables() {
        existingTransaction {
            SchemaUtils.drop(*tables)
        }
    }

    suspend fun <T> newTransaction(
        block: suspend (transaction: Transaction) -> T
    ): T {
        return newSuspendedTransaction(db = database) {
            block(this)
        }
    }

    suspend fun <T> existingTransaction(block: suspend (transaction: Transaction) -> T): T {
        val transaction = TransactionManager.current()
        return transaction.suspendedTransaction {
            block(this)
        }
    }

    suspend fun <T> transaction(block: suspend (transaction: Transaction) -> T): T {
        val transaction = TransactionManager.currentOrNull()
        return if (transaction == null || transaction.connection.isClosed) {
            newSuspendedTransaction(db = database) {
                block(this)
            }
        } else {
            transaction.suspendedTransaction {
                block(this)
            }
        }
    }
}