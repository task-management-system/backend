package kz.seasky.tms.model.statistic

import org.joda.time.DateTime

data class Statistic(
    val task: Task,
    val disk: Disk,
    val createdAt: DateTime
) {
    data class Task(val all: Status, val actual: Status) {
        data class Status(
            val new: Long,
            val inWork: Long,
            val canceled: Long,
            val closed: Long
        )
    }

    data class Disk(val available: Long, val used: Long)
}