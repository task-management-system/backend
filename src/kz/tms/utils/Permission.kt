package kz.tms.utils

class Permission {
    companion object {
        //@formatter:off
        const val ViewTask  : Long = 1 shl 0
        const val CreateTask: Long = 1 shl 1
        const val DeleteTask: Long = 1 shl 2
        const val ViewUser  : Long = 1 shl 3
        const val InsertUser: Long = 1 shl 4
        const val DeleteUser: Long = 1 shl 5
        //@formatter:on

        val pair = allKeysAndValues()
    }
}

private fun allKeys(): List<String> = listOf(
    "ViewTask",
    "CreateTask",
    "DeleteTask",
    "ViewUser",
    "InsertUser",
    "DeleteUser"
)

private fun allValues(): List<Long> = listOf(
    Permission.ViewTask,
    Permission.CreateTask,
    Permission.DeleteTask,
    Permission.ViewUser,
    Permission.InsertUser,
    Permission.DeleteUser
)

internal fun allKeysAndValues(): List<Pair<String, Long>> {
    val pairs = arrayListOf<Pair<String, Long>>()

    val keys = allKeys()
    val values = allValues()

    if (keys.size != values.size) throw RuntimeException()

    for (i in keys.indices) {
        pairs.add(keys[i] to values[i])
    }

    return pairs
}