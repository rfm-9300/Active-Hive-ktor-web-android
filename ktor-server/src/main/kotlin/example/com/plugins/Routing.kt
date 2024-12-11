package example.com.plugins

import example.com.data.db.user.UserRepository
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
    tokenConfig: TokenConfig
) {
    routing {
        homeRoute()
        login(hashingService, userRepository, tokenService, tokenConfig)
        singUp(hashingService, userRepository)
        authenticate()
        getSecretInfo()
    }
}


