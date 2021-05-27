package kz.seasky.tms.database.tables.user

import kotlinx.uuid.exposed.KotlinxUUIDTable
import kz.seasky.tms.database.tables.role.RoleTable
import org.jetbrains.exposed.sql.jodatime.datetime

object UserTable : KotlinxUUIDTable("user") {
    val username = varchar("username", 32).uniqueIndex("uq_user_username")
    val password = text("password").nullable()
    val name = varchar("name", 255).nullable()
    val email = varchar("email", 255).nullable()
    val isActive = bool("is_active")
    val role = reference("role_id", RoleTable, fkName = "fk_user_role_id_id")
    val createdAt = datetime("created_at")
    val avatar = text("avatar").nullable()

    override val primaryKey by lazy { PrimaryKey(UserTable.id, name = "pk_user_id") }
}