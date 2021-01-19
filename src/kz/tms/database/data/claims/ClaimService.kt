package kz.tms.database.data.claims

import kz.tms.database.TransactionService
import kz.tms.model.claim.Claim

class ClaimService(
    private val transactionService: TransactionService,
    private val repository: ClaimRepository
) {
    suspend fun getAll(): List<Claim> {
        return transactionService.transaction {
            repository.getAll()
        }
    }
}