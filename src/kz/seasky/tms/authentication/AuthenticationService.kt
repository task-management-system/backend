package kz.seasky.tms.authentication

import kz.seasky.tms.database.data.role.RoleService
import kz.seasky.tms.database.data.user.UserService
import kz.seasky.tms.database.data.user.merge
import kz.seasky.tms.model.Response
import kz.seasky.tms.model.authentication.AuthenticationCredential
import kz.seasky.tms.model.authentication.AuthenticationResponse
import kz.seasky.tms.utils.JWTConfig

class AuthenticationService(
    private val jwtConfig: JWTConfig,
    private val userService: UserService,
    private val roleService: RoleService
) {
    suspend fun authenticate(credentials: AuthenticationCredential): Response<AuthenticationResponse> {
        val user = userService.getByUsernameOrByEmailOrNull(credentials.usernameOrEmail) ?: return Response.Error(
            message = "Пользователь с указанным логином/почтой не найден, проверьте корректность введенных данных"
        )

        if (!user.isActive!!) return Response.Error(
            message = "Пользователь неактивен, обратитесь к администратору"
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