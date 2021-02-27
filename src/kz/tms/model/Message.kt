package kz.tms.model

import kz.tms.enums.MessageType

data class Message(
    val type: MessageType,
    val text: String,
    val stackTrace: String? = null
) {

    //TODO Standardize
    companion object {
        const val FILL_PAYLOAD =
            "Полезная нагрузка либо пуста, либо некорректена заполнена. Заполните полезную нагрузку или свертись с документацией данной конечной точки (Документации нет, но вы держитесь)"

        const val PRINCIPAL_NOT_FOUND = "Не удалось получить аутентификационные данные (Кыш атседава)"

        const val INDICATE_USER_ID = "Вы забыли указать идентификатор пользователя (Бывает такое)"
    }
}