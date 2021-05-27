package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*

fun Route.docs() {
    route("/docs") {
        get {
            call.respondHtml {
                body {
                    h1 {
                        +"Добро пожаловать на страничку документации TMS API"
                    }
                    h2 {
                        +"Аутентификация"
                    }
                    p {
                        +"Для аутентификация используется JWT."
                        br()
                        +"Все запросы работают только с валидным токеном."
                    }
                    h1 {
                        +"Ресурсы"
                    }
                    hr()
                    h2 {
                        a(href = "/api/v1/docs/authentication") {
                            +"Аутентификация"
                        }
                    }
                    table {
                        thead {
                            tr {
                                th {
                                    +"Ресурс"
                                }
                                th {
                                    +"Описание"
                                }
                            }
                        }
                        tbody {
                            tr {
                                td {
                                    a(href = "/api/v1/docs/authentication") {
                                        +"GET /api/v1/authentication"
                                    }
                                }
                                td {
                                    +"Генератор токена"
                                }
                            }
                        }
                    }

                    h2 {
                        a(href = "/api/v1/docs/user") {
                            +"Пользователи"
                        }
                    }
                    table {
                        thead {
                            tr {
                                th {
                                    +"Ресурс"
                                }
                                th {
                                    +"Описание"
                                }
                            }
                        }
                        tbody {
                            tr {
                                td {
                                    a(href = "/api/v1/docs/user/users") {
                                        +"GET /api/v1/users"
                                    }
                                }
                                td {
                                    +"Список пользователей"
                                }
                            }
                        }
                    }
                }
            }
        }

        route("authentication") {
            get("/") {
                call.respondHtml {
                    head {
                        style {
                            unsafe {
                                raw(
                                    """
                                    .pretty {
                                        padding: 8px;
                                        background-color: #f7f7f9;
                                        border: 1px solid #e1e1e8;
                                    }
                                """.trimIndent()
                                )
                            }
                        }
                    }
                    body {
                        h1 {
                            +"Аутентификация"
                        }
                        table {
                            thead {
                                tr {
                                    th {
                                        +"Ресурс"
                                    }
                                    th {
                                        +"Описание"
                                    }
                                }
                            }
                            tbody {
                                tr {
                                    td {
                                        a(href = "/api/v1/docs/authentication") {
                                            +"POST /api/v1/authentication"
                                        }
                                    }
                                    td {
                                        +"Генератор токена"
                                    }
                                }
                            }
                        }
                        h2 {
                            +"Параметры"
                        }
                        hr()
                        p {
                            +"Отсутсвуют"
                        }
                        h2 {
                            +"Данные запроса"
                        }
                        hr()
                        pre(classes = "pretty") {
                            +"""
                                    {
                                        "username": "username",
                                        "password": "password"
                                    }
                                """.trimIndent()
                        }
                        h2 {
                            +"Результат запроса"
                        }
                        hr()
                        pre(classes = "pretty") {
                            +"""
                                {
                                    "message": {
                                        "type": "Success",
                                        "text": "Аутентификация прошла успешно"
                                    },
                                    "data": {
                                        "token": "token"
                                    }
                                }
                            """.trimIndent()
                        }
                    }
                }
            }
        }

        route("/user") {
            get("/") {
                call.respondText("Документация в разработке")
            }

            get("/users") {
                call.respondText("Документация в разработке")
            }
        }
    }
}