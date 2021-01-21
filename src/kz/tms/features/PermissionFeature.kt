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
        internal var permission: (AuthenticationPrincipal) -> Long = { 0L }

        fun register(block: (AuthenticationPrincipal) -> Long) {
            permission = block
        }
    }

    fun interceptPipeline(
        pipeline: ApplicationCallPipeline,
        permission: Long
    ) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Features, Authentication.ChallengePhase)
        pipeline.insertPhaseAfter(Authentication.ChallengePhase, AuthorizationPhase)

        pipeline.intercept(AuthorizationPhase) {
            val principal = call.authentication.principal<AuthenticationPrincipal>()
                ?: throw PermissionException("У вас недостаточно прав для данной операции")
            val role = permission(principal)

            if (permission and role == 0L) throw PermissionException("Not stonk enough, gerarahere bitch")
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
    permission: Long,
    build: Route.() -> Unit
): Route {
    val authorizedRoute = createChild(PermissionRouteSelector(permission.toString()))
    application.feature(PermissionFeature).interceptPipeline(authorizedRoute, permission)
    authorizedRoute.build()
    return authorizedRoute
}

fun Route.withPermission(permission: Long, build: Route.() -> Unit): Route {
    return authorizedRoute(permission = permission, build = build)
}

//TODO А оно нужно?
//fun Route.withoutRole(vararg roles: Long, build: Route.() -> Unit): Route {
//    return authorizedRoute()
//}