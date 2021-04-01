package kz.seasky.tms.model.role

class RoleUpdate(
    val id: Short,
    override val power: Long,
    override val meaning: String
) : RoleValidator(power, meaning)