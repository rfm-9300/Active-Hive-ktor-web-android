package example.com.plugins

import example.com.authenticate
import example.com.data.user.UserRepository
import example.com.getSecretInfo
import example.com.login
import example.com.security.hashing.HashingService
import example.com.security.token.TokenConfig
import example.com.security.token.TokenService
import example.com.singUp
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userRepository: UserRepository,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        login(hashingService, userRepository, tokenService, tokenConfig)
        singUp(hashingService, userRepository)
        authenticate()
        getSecretInfo()
    }
}
