package kz.tms.database.data.task

import kz.tms.database.TransactionService
import kz.tms.model.paging.Paging
import kz.tms.model.task.ITask
import kz.tms.model.task.TaskEntity

class TaskService(
    private val transactionService: TransactionService,
    private val repository: TaskRepository
) {
    suspend fun count(userId: Long): Long {
        return transactionService.transaction {
            repository.count(userId)
        }
    }

    suspend fun getAll(userId: Long, paging: Paging): List<TaskEntity> {
        return transactionService.transaction {
            repository.getAll(userId, paging)
        }
    }

    suspend fun insert(task: ITask): TaskEntity? {
        return transactionService.transaction {
            repository.insert(task)
        }
    }

    suspend fun delete(id: Long): Int {
        return transactionService.transaction {
            repository.delete(id)
        }
    }
}