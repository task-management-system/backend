package kz.seasky.tms.repository.detail

import kz.seasky.tms.database.TransactionService

class DetailService(
    private val transactionService: TransactionService,
    private val repository: DetailRepository
) {
//    suspend fun count(userId: Long, statusId: Short): Long {
//        return transactionService.transaction {
//            repository.count(userId, statusId)
//        }
//    }
//
//    suspend fun getAll(userId: Long, statusId: Short, paging: Paging): List<TaskWithCreatorAndDetailId> {
//        return transactionService.transaction {
//            repository.getAll(userId, statusId, paging)
//        }
//    }
//
//    suspend fun batchInsert(details: List<DetailCreate>): Int {
//        return transactionService.transaction {
//            repository.batchInsert(details)
//        }.size
//    }
}