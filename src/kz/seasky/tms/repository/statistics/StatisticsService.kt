package kz.seasky.tms.repository.statistics

import io.ktor.sessions.*
import kz.seasky.tms.model.statistics.Statistics

interface StatisticsService {
    suspend fun getAndUpdateSession(session: CurrentSession): Statistics
}