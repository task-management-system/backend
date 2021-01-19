package kz.tms.model.authentication

import com.google.gson.annotations.SerializedName
import kz.tms.model.user.UserResponse

data class AuthenticationResponse(
    val token: String,
    @SerializedName("user")
    val userResponse: UserResponse
)