package kz.tms.model.response

import kz.tms.enums.MessageType

data class Response<T>(
    val message: Message?,
    val data: T
)

class Message(
    val type: MessageType,
    val text: String
)

