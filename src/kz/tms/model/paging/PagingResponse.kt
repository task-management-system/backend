package kz.tms.model.paging

data class PagingResponse<T>(
    val total: Long,
    val list: T
)