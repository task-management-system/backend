package kz.tms.authentication

import kz.tms.database.data.role.RoleService
import kz.tms.database.data.user.UserService
import kz.tms.database.data.user.merge
import kz.tms.model.Response
import kz.tms.model.authentication.AuthenticationCredential
import kz.tms.model.authentication.AuthenticationResponse
import kz.tms.utils.JWTConfig

class AuthenticationService(
    private val jwtConfig: JWTConfig,
    private val userService: UserService,
    private val roleService: RoleService
) {
    suspend fun authenticate(credentials: AuthenticationCredential): Response<AuthenticationResponse> {
        val user = userService.getByUsernameOrByEmailOrNull(credentials.usernameOrEmail) ?: return Response.Error(
            message = "Пользователь с указанным логином/почтой не найден, проверьте корректность введенных данных"
        )

        if (!user.isActive) return Response.Error(
            message = "Пользователь неактивен"
        )

        return when (credentials.password == user.password) {
            true -> {
                val token = jwtConfig.makeToken(user.username)
                val role = roleService.getRoleById(user.roleId)
                Response.Success(
                    message = "Аутентификация прошла успешно",
                    data = AuthenticationResponse(token, user merge role)
                )
            }
            false -> Response.Error(
                message = "Вы ввели неверный пароль"
            )
        }
    }
}