package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.tms.database.data.task.TaskService
import kz.tms.model.paging.PagingResponse
import kz.tms.utils.asPaging
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.task() {
    val service: TaskService by inject()

    route("/task") {

    }

    route("/tasks") {
        get {
            val paging = call.parameters.asPaging()

            call.success(
                data = PagingResponse(
                    total = service.count(),
                    list = service.getAll(paging)
                )
            )
        }
    }
}