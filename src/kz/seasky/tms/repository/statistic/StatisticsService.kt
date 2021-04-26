package kz.seasky.tms.repository.statistic

import io.ktor.sessions.*
import kz.seasky.tms.model.statistic.Statistics
import org.joda.time.DateTime

interface StatisticsService {
    suspend fun getAndUpdateSession(sessions: CurrentSession): Statistics
}

class StatisticsServiceImpl(private val repository: StatisticRepository) : StatisticsService {
    @Suppress("UnnecessaryVariable")
    override suspend fun getAndUpdateSession(sessions: CurrentSession): Statistics {
        val statistics = Statistics(
            task = Statistics.Task(repository.getTasksAll(), repository.getTasksActual()),
            disk = Statistics.Disk(repository.getFileUsedSpace(), repository.getFileAvailableSpace()),
            createdAt = DateTime.now().millis
        )

        sessions.set(statistics)

        return statistics
    }
}