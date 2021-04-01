package kz.seasky.tms.enums

enum class BuildVariant(val fileName: String) {
    Product("application.conf"),
    Develop("application-dev.conf")
}