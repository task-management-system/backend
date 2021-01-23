package kz.tms.model.paging

data class PagingResponse<T>(
    val totalCount: Long,
    val currentPage: Long,
    val currentSize: Int,
    val data: T
)