package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kz.tms.database.data.task.TaskService
import kz.tms.model.paging.Paging
import kz.tms.model.paging.PagingResponse
import kz.tms.utils.error
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.task() {
    val service: TaskService by inject()

    route("/task") {

    }

    route("/tasks") {
        get {
            val paging = call.receiveOrNull<Paging>() ?: return@get call.error<Nothing>(
                message = "Пейлоад чист, а значит пошел нахуй"
            )

            val result = paging.validate()
            if (!result.isNullOrEmpty()) return@get call.error<Nothing>(
                message = result
            )

            call.success(
                data = PagingResponse(
                    totalCount = service.count(),
                    list = service.getAll(paging)
                )
            )
        }
    }
}