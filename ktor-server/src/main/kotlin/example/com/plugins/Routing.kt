package example.com.plugins

import example.com.data.db.event.EventRepository
import example.com.data.db.user.UserRepository
import example.com.data.utils.SseManager
import example.com.routes.*
import example.com.security.hashing.HashingService
import example.com.security.token.TokenConfig
import example.com.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRouting(
    userRepository: UserRepository,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    sseManager: SseManager,  // Add these
    eventRepository: EventRepository
)  {
    routing {
        homeRoutes(sseManager, eventRepository)
        loginRoutes(hashingService, userRepository, tokenService, tokenConfig)
        eventRoutes(eventRepository, sseManager, userRepository)
        dynamicJsProcessing()
    }
}