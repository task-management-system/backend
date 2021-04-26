package kz.seasky.tms.repository.statistic

import io.ktor.sessions.*
import kz.seasky.tms.model.statistic.Statistic
import org.joda.time.DateTime

interface StatisticService {
    suspend fun getAndUpdateSession(sessions: CurrentSession): Statistic
}

class StatisticServiceImpl(private val repository: StatisticRepository) : StatisticService {
    @Suppress("UnnecessaryVariable")
    override suspend fun getAndUpdateSession(sessions: CurrentSession): Statistic {
        val statistic = Statistic(
            task = Statistic.Task(repository.getTasksAll(), repository.getTasksActual()),
            disk = Statistic.Disk(repository.getFileUsedSpace(), repository.getFileAvailableSpace()),
            createdAt = DateTime.now().millis
        )

        sessions.set(statistic)

        return statistic
    }
}