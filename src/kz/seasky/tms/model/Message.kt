package kz.seasky.tms.model

import kz.seasky.tms.enums.MessageType

data class Message(
    val type: MessageType,
    val text: String,
    val stackTrace: String? = null
) {

    //TODO Standardize
    companion object {
        const val FILL_PAYLOAD =
            "Полезная нагрузка либо пуста, либо некорректено заполнена; Заполните полезную нагрузку или свертись с документацией"

        const val PRINCIPAL_NOT_FOUND = "Не удалось получить аутентификационные данные"

        const val INDICATE_ID = "Вы забыли указать идентификатор "
    }
}