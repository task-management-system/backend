package kz.seasky.tms.repository.authentication

import kz.seasky.tms.model.authentication.AuthenticationCredential
import kz.seasky.tms.model.authentication.AuthenticationResponse

interface AuthenticationService {
    suspend fun authenticate(credentials: AuthenticationCredential): AuthenticationResponse
}