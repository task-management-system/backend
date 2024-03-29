package kz.seasky.tms.utils

class Permission(
    val name: String,
    val power: Long,
    val description: String
) {
    /**
     * After adding new permission
     * Don't forget update [allPermissions] list
     */
    companion object {
        //@formatter:off
        val Administrator = Permission("Administrator",1L shl 0, "Администрирование")
        val ViewTask      = Permission("ViewTask",     1L shl 1, "Просмотр задач")
        val CreateTask    = Permission("CreateTask",   1L shl 2, "Создание задач")
        val DeleteTask    = Permission("DeleteTask",   1L shl 3, "Удаление задач")
        val ViewUser      = Permission("ViewUser",     1L shl 4, "Просмотр пользователя")
        val InsertUser    = Permission("InsertUser",   1L shl 5, "Добавление пользователя")
        val DeleteUser    = Permission("DeleteUser",   1L shl 6, "Удаление пользователя")
        val UpdateUser    = Permission("UpdateUser",   1L shl 7, "Обновление пользователя")
        //@formatter:on

        val all = allPermissions()
        val summaryPower = all.sumBy(Permission::power)
    }
}

private inline fun <T> Iterable<T>.sumBy(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

internal fun allPermissions() = listOf(
    Permission.Administrator,
    Permission.ViewTask,
    Permission.CreateTask,
    Permission.DeleteTask,
    Permission.ViewUser,
    Permission.InsertUser,
    Permission.DeleteUser,
    Permission.UpdateUser
)