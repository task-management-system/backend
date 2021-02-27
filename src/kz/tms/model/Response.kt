package kz.tms.model

import kz.tms.enums.MessageType

sealed class Response<out T>(
    val message: Message?,
    val data: T?
) {
    class Success<T>(
        message: String? = null,
        data: T
    ) : Response<T>(
        message?.let { Message(MessageType.Success, message) },
        data
    )

    class Error<T>(
        message: String? = null,
        data: T? = null,
        stackTrace: String? = null
    ) : Response<T>(
        message?.let { Message(MessageType.Error, message, stackTrace) },
        data
    )

    class Warning<T>(
        message: String? = null,
        data: T? = null
    ) : Response<T>(
        message?.let { Message(MessageType.Warning, message) },
        data
    )
}