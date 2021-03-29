package kz.seasky.tms.repository.authentication

import kz.seasky.tms.enums.AuthenticationType
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.model.authentication.AuthenticationCredential
import kz.seasky.tms.model.authentication.AuthenticationResponse
import kz.seasky.tms.utils.JWTConfig

interface AuthenticationService {
    suspend fun authenticate(credentials: AuthenticationCredential): AuthenticationResponse
}

class AuthenticationServiceImpl(
    private val jwtConfig: JWTConfig,
    private val repository: AuthenticationRepository
) : AuthenticationService {
    override suspend fun authenticate(credentials: AuthenticationCredential): AuthenticationResponse {
        val user = repository.getUser(credentials) ?: when (credentials.type) {
            AuthenticationType.Username -> throw ErrorException("Пользователь с указанным логином не найден, проверьте корректность введенных данных")
            AuthenticationType.Email -> throw ErrorException("Пользователь с указанной почтой не найден, проверьте корректность введенных данных")
        }

        if (!user.isActive) throw ErrorException("Пользователь неактивен, обратитесь к администратору")

        if (!repository.validatePassword(
                user.id,
                credentials.password
            )
        ) throw ErrorException("Неверный пароль, проверьте корректность введенных данных")

        val token = jwtConfig.makeToken(user.id)
        return AuthenticationResponse(token, user)
    }
}