package kz.seasky.tms.database.utils

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable

abstract class ShortEntity(id: EntityID<Short>) : Entity<Short>(id)

abstract class ShortEntityClass<out E : ShortEntity>(
    table: IdTable<Short>,
    entityType: Class<E>? = null
) : EntityClass<Short, E>(table, entityType)