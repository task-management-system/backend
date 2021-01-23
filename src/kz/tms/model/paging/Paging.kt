package kz.tms.model.paging

import org.jetbrains.exposed.sql.SortOrder

data class Paging(
    val page: Long,
    val size: Int,
    val order: String? = null
) {
    /** Available constants [[MinPage], [MinSize], [MaxSize]] */
    companion object {
        /** Equals to 1 */
        const val MinPage = 1

        /** Equals to 5 */
        const val MinSize = 5

        /** Equals to 50 */
        const val MaxSize = 50
    }

    private var _limit: Int = 0

    /** Formula (limit = [size]) */
    val limit: Int get() = _limit

    private var _offset: Long = 0L

    /** Formula offset = (([page] - 1) * [limit]) */
    val offset: Long get() = _offset

    private var _sortOrder: SortOrder = SortOrder.ASC

    /** Default value is [SortOrder.ASC] */
    val sortOrder: SortOrder get() = _sortOrder

    /**
     * Provides validation with constant defined in [Companion] object
     *
     * Setup [limit], [offset] and [sortOrder] values
     *
     * The validation is considered passed if the returned value is null
     */
    fun validate(): String? {
        return when {
            page < MinPage -> "Минимальная значение page - $MinPage"
            size < MinSize -> "Минимальный значение size - $MinSize"
            size > MaxSize -> "Максимальный значение page - $MaxSize"
            else -> {
                _limit = size
                _offset = page.dec() * limit
                _sortOrder = order.toSortOrder()
                null
            }
        }
    }

    @Suppress("MoveVariableDeclarationIntoWhen")
    private fun String?.toSortOrder(): SortOrder {
        if (this == null) return SortOrder.ASC

        val upperCase = this.toUpperCase()
        return when (upperCase) {
            "ASC" -> SortOrder.ASC
            "DESC" -> SortOrder.DESC
            else -> throw IllegalArgumentException("Неверный ключ сортировки, ожидалось 'ASC' или 'DESC' получено '$upperCase'")
        }
    }
}