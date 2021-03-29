package kz.seasky.tms.repository.role

class RoleRepository {
//    fun getAllOrEmpty(): List<Role> {
//        return RoleTable
//            .selectAll()
//            .map { toRole(it) }
//    }

//    fun getIdByPowerOrNull(power: Int): Long? {
//        return RoleTable
//            .select { RoleTable.power eq power }
//            .map { it[RoleTable.id] }
//            .singleOrNull()
//    }

//    fun getRoleById(id: Long): Role {
//        return RoleTable
//            .select { RoleTable.id eq id }
//            .map { toRole(it) }
//            .single()
//    }

//    fun insert(role: Role): List<ResultRow>? {
//        return RoleTable
//            .insert { insertStatement ->
//                insertStatement.toRole(role)
//            }.resultedValues
//    }
//
//    fun batchInsert(roles: List<Role>): List<ResultRow> {
//        return RoleTable
//            .batchInsert(roles) { role ->
//                toRole(role)
//            }
//    }

//    fun update(id: Long, role: Role): Int {
//        return RoleTable
//            .update(
//                where = { RoleTable.id eq id },
//                body = { statement -> statement.toRole(role) }
//            )
//    }
}