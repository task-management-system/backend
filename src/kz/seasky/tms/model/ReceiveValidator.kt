package kz.seasky.tms.model

interface ReceiveValidator {
    fun <T> validate(): T
}