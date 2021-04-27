package kz.seasky.tms.repository.statistics

import io.ktor.sessions.*
import kz.seasky.tms.model.statistics.Statistics
import org.joda.time.DateTime

class StatisticsServiceImpl(private val repository: StatisticsRepository) : StatisticsService {
    @Suppress("UnnecessaryVariable")
    override suspend fun getAndUpdateSession(session: CurrentSession): Statistics {
        val statistics = Statistics(
            task = Statistics.Task(repository.getTasksAll(), repository.getTasksActual()),
            disk = Statistics.Disk(repository.getFileUsedSpace(), repository.getFileAvailableSpace()),
            createdAt = DateTime.now().millis
        )

        session.set(statistics)

        return statistics
    }
}