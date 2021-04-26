package kz.seasky.tms.repository.statistic

import kz.seasky.tms.model.statistic.Statistics
import kz.seasky.tms.repository.task.TaskService
import kz.seasky.tms.utils.FileHelper

class StatisticRepository(
    private val taskService: TaskService,
    private val fileHelper: FileHelper
) {
    suspend fun getTasksAll(): Statistics.Task.Status {
        return taskService.getAllCount()
    }

    suspend fun getTasksActual(): Statistics.Task.Status {
        return taskService.getActualCount()
    }

    suspend fun getFileUsedSpace(): Long {
        return fileHelper.usedSpace()
    }

    suspend fun getFileAvailableSpace(): Long {
        return fileHelper.availableSpace()
    }
}