package kz.tms.database.data.claims

import org.jetbrains.exposed.sql.ResultRow

fun toClaim(resultRow: ResultRow): Claim {
    return Claim(
        name = resultRow[ClaimsTable.name],
        power = resultRow[ClaimsTable.power],
        text = resultRow[ClaimsTable.text]
    )
}