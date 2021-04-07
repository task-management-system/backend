package kz.seasky.tms.database

import org.jetbrains.exposed.sql.Transaction

interface TransactionService {
    suspend fun <T> newTransaction(block: suspend () -> T): T

    suspend fun <T> existingTransaction(block: suspend () -> T): T

    suspend fun <T> transaction(block: suspend (transaction: Transaction) -> T): T
}

internal class TransactionServiceImpl(
    private val databaseConnector: DatabaseConnector
) : TransactionService {

    override suspend fun <T> newTransaction(block: suspend () -> T) = databaseConnector.newTransaction {
        block()
    }

    override suspend fun <T> existingTransaction(block: suspend () -> T) = databaseConnector.existingTransaction {
        block()
    }

    override suspend fun <T> transaction(block: suspend (transaction: Transaction) -> T) =
        databaseConnector.transaction {
            block(it)
        }
}