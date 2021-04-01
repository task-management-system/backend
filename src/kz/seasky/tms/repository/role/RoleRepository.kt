package kz.seasky.tms.repository.role

import kz.seasky.tms.database.tables.role.RoleEntity
import kz.seasky.tms.model.role.Role
import kz.seasky.tms.model.role.RoleInsert
import kz.seasky.tms.model.role.RoleUpdate

class RoleRepository {
    fun getAll(): List<Role> {
        return RoleEntity
            .all()
            .map(RoleEntity::toRole)
    }

    fun insert(role: RoleInsert): Role {
        return RoleEntity
            .insert(role)
    }

    fun update(role: RoleUpdate): Role {
        return RoleEntity
            .update(role)
    }

    fun delete(id: Short) {
        RoleEntity[id].delete()
    }

    fun batchInsert(roles: List<RoleInsert>): List<Role> {
        return RoleEntity
            .batchInsert(roles)
    }
}