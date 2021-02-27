package kz.tms.model

import kz.tms.enums.MessageType

data class Message(
    val type: MessageType,
    val text: String,
    val stackTrace: String? = null
) {
    companion object {
        const val FILL_PAYLOAD =
            "Полезная нагрузка либо пуста, либо некорректена заполнена. Заполните полезную нагрузку или свертись с документацией данной конечной точки (Документации нет, но вы держитесь)"

        const val INDICATE_USER_ID = "Вы забыли указать идентификатор пользователя (Бывает такое)"
    }
}