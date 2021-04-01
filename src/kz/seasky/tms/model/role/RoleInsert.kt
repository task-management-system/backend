package kz.seasky.tms.model.role

class RoleInsert(
    override val power: Long,
    override val meaning: String
) : RoleValidator(power, meaning)