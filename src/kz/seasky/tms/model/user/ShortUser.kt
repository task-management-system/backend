package kz.seasky.tms.model.user

import kz.seasky.tms.model.WithId

interface ShortUser : WithId {
    val username: String
}