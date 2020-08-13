package org.gridiron.backend.ktor

import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.application.feature
import io.ktor.auth.Authentication.Feature.AuthenticatePhase
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.RouteSelector
import io.ktor.routing.RouteSelectorEvaluation
import io.ktor.routing.RoutingResolveContext
import io.ktor.routing.application
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.PipelinePhase
import org.gridiron.backend.model.User
import org.slf4j.LoggerFactory

class RoleBasedAuthorizer {
    lateinit var authorizationFunction: suspend ApplicationCall.(Set<User.Role>) -> Boolean

    fun validate(body: suspend ApplicationCall.(Set<User.Role>) -> Boolean) {
        authorizationFunction = body
    }
}

class RoleAuthorization(config: Configuration) {
    private val log = LoggerFactory.getLogger(RoleAuthorization::class.java)
    private val config = config.copy()

    constructor(provider: RoleBasedAuthorizer) : this(Configuration(provider))

    @KtorExperimentalAPI
    fun interceptPipeline(pipeline: ApplicationCallPipeline, roles: Set<User.Role>) {
        pipeline.addPhase(AuthenticatePhase)
        pipeline.insertPhaseAfter(AuthenticatePhase, authorizationPhase)
        pipeline.intercept(authorizationPhase) {
            if (!config.provider.authorizationFunction(call, roles)) {
                log.info("User is not authorized, responding Forbidden")
                call.respond(HttpStatusCode.Forbidden)
                finish()
            }
        }
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, RoleBasedAuthorizer, RoleAuthorization> {
        private val authorizationPhase = PipelinePhase("Authorization")
        override val key: AttributeKey<RoleAuthorization> = AttributeKey("RoleAuthorization")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: RoleBasedAuthorizer.() -> Unit
        ): RoleAuthorization {
            return RoleAuthorization(RoleBasedAuthorizer().apply(configure))
        }
    }

    class Configuration(val provider: RoleBasedAuthorizer) {
        internal fun copy(): Configuration = Configuration(provider)
    }
}

@KtorExperimentalAPI
fun Route.rolesAllowed(vararg roles: User.Role, build: Route.() -> Unit): Route {
    val authorizedRoute = createChild(object : RouteSelector(RouteSelectorEvaluation.qualityConstant) {
        override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant
    })

    application.feature(RoleAuthorization).interceptPipeline(authorizedRoute, roles.toSet())

    authorizedRoute.build()
    return authorizedRoute
}
