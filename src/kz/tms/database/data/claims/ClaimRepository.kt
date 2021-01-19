package kz.tms.database.data.claims

import kz.tms.model.claim.Claim
import org.jetbrains.exposed.sql.selectAll

class ClaimRepository {
    fun getAll(): List<Claim> {
        return ClaimsTable
            .selectAll()
            .map {
                toClaim(it)
            }
    }
}