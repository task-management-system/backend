package kz.tms.features

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kz.tms.exceptions.PermissionException
import kz.tms.model.authentication.AuthenticationPrincipal

class PermissionFeature(config: Configuration) {
    private val permission = config.permission

    class Configuration {
        internal var permission: (AuthenticationPrincipal) -> Int = { 0 }

        fun register(block: (AuthenticationPrincipal) -> Int) {
            permission = block
        }
    }

    fun interceptPipeline(
        pipeline: ApplicationCallPipeline,
        permission: Int
    ) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Features, Authentication.ChallengePhase)
        pipeline.insertPhaseAfter(Authentication.ChallengePhase, AuthorizationPhase)

        pipeline.intercept(AuthorizationPhase) {
            val principal = call.authentication.principal<AuthenticationPrincipal>()
                ?: throw PermissionException("Не удалось найти авторизационные данные")
            val role = permission(principal)

            if (permission and role == 0) throw PermissionException("У вас недостаточно прав для данной операции")
        }
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, PermissionFeature> {
        override val key = AttributeKey<PermissionFeature>("PermissionFeature")

        val AuthorizationPhase = PipelinePhase("Authorization")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): PermissionFeature {
            val configuration = Configuration().apply(configure)
            return PermissionFeature(configuration)
        }
    }
}

class PermissionRouteSelector(
    private val description: String
) : RouteSelector(RouteSelectorEvaluation.qualityConstant) {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

    override fun toString(): String = "(permission ${description})"
}

private fun Route.authorizedRoute(
    permission: Int,
    build: Route.() -> Unit
): Route {
    val authorizedRoute = createChild(PermissionRouteSelector(permission.toString()))
    application.feature(PermissionFeature).interceptPipeline(authorizedRoute, permission)
    authorizedRoute.build()
    return authorizedRoute
}

fun Route.withPermission(permission: Int, build: Route.() -> Unit): Route {
    return authorizedRoute(permission = permission, build = build)
}