package kz.tms.database

interface TransactionService {
    suspend fun <T> newTransaction(block: suspend () -> T): T

    suspend fun <T> existingTransaction(block: suspend () -> T): T

    suspend fun <T> transaction(block: suspend () -> T): T
}