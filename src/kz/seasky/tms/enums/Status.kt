package kz.seasky.tms.enums

enum class Status(val value: Short) {
    New(1),
    InWork(2),
    Canceled(3),
    Closed(4),
    Prepared(5)
}