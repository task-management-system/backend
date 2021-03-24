package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.uuid.UUID
import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.database.data.DaoUserAddAndUpdate
import kz.seasky.tms.database.data.DaoUsers
import kz.seasky.tms.database.data.DaoUsersEntity
import kz.seasky.tms.extensions.success
import kz.seasky.tms.model.Message
import org.jetbrains.exposed.sql.SortOrder
import org.koin.ktor.ext.inject

fun Route.v1() {
    val service: TransactionService by inject()

    route("/api/v1") {
        docs()

        authentication()

        route("/dao") {
            get("/all") {
                val result = service.transaction {
                    return@transaction DaoUsersEntity.all().orderBy(DaoUsers.id to SortOrder.DESC)
                        .map { it.toDaoUser() }
                }

                call.success(data = result)
            }

            get {
                val id = call.parameters["id"] ?: "be4fffb6-8d83-4560-9ed0-741bdf265d12"
                val result = service.transaction {
                    return@transaction DaoUsersEntity.findById(UUID(id))?.toDaoUser()
                }

                call.success(data = result)
            }

            put {
                val newUser = call.receive<DaoUserAddAndUpdate>()
                val result = service.transaction {
                    return@transaction DaoUsersEntity.new {
                        toDaoUsersEntity(newUser)
                    }.toDaoUser()
                }

                call.success(data = result)
            }

            patch {
                val id = call.getId()
                val newUser = call.receive<DaoUserAddAndUpdate>()
                val result = service.transaction {
                    return@transaction DaoUsersEntity.findById(id)?.toDaoUsersEntity(newUser)?.toDaoUser()
                }

                call.success(data = result)
            }

            delete {
                val id = call.getId()
                service.transaction {
                    return@transaction DaoUsersEntity.findById(id)?.delete()
                } ?: throw IndicateIdException("Не удалось удалить запись")

                call.success<Nothing>(message = Message.DATA_SUCCESSFULLY_DELETED)
            }
        }

        authenticate("token") {
            permission()

            role()

            status()

            task()

            user()
        }
    }
}

class IndicateIdException(override val message: String?) : Exception(message)

@Throws(IndicateIdException::class)
fun ApplicationCall.getId(idName: String = "id"): UUID {
    return UUID(parameters[idName] ?: throw IndicateIdException(Message.INDICATE_ID + idName))
}