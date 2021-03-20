package kz.seasky.tms.model.authentication

import com.google.gson.annotations.SerializedName
import kz.seasky.tms.model.user.UserWithRole

data class AuthenticationResponse(
    val token: String,
    @SerializedName("user")
    val userResponse: UserWithRole
)