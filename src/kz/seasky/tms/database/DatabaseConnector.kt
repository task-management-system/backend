package kz.seasky.tms.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransaction
import javax.sql.DataSource

class DatabaseConnector(dataSource: DataSource) {
    private val database = Database.connect(dataSource)

//    FIXME This initial block useless or I missing something 
//
//    private val tables = arrayOf(
//        UserTable,
//        RoleTable,
//        TaskTable,
//        StatusTable,
//        DetailTable,
//    )

//    init {
//        runBlocking {
//            newTransaction {
//                SchemaUtils.createMissingTablesAndColumns(*tables)
//            }
//        }
//    }

//    suspend fun deleteAllTables() {
//        existingTransaction {
//            SchemaUtils.drop(*tables)
//        }
//    }

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