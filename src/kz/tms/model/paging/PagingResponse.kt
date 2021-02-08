package kz.tms.model.paging

data class PagingResponse<T>(
    val totalCount: Long,
    val list: T
)