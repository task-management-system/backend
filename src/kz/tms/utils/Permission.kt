package kz.tms.utils

class Permission(
    val name: String,
    val power: Int,
    val description: String
) {
    companion object {
        //@formatter:off
        val ViewTask   = Permission("ViewTask",   1 shl 0, "Просмотр задач"         )
        val CreateTask = Permission("CreateTask", 1 shl 1, "Создание задач"         )
        val DeleteTask = Permission("DeleteTask", 1 shl 2, "Удаление задач"         )
        val ViewUser   = Permission("ViewUser",   1 shl 3, "Просмотр пользователя"  )
        val InsertUser = Permission("InsertUser", 1 shl 4, "Добавление пользователя")
        val DeleteUser = Permission("DeleteUser", 1 shl 5, "Удаление пользователя"  )
        //@formatter:on

        val all = allPermissions()
    }
}

internal fun allPermissions() = listOf(
    Permission.ViewTask,
    Permission.CreateTask,
    Permission.DeleteTask,
    Permission.ViewUser,
    Permission.InsertUser,
    Permission.DeleteUser
)