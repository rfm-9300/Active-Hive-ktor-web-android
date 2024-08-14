package example.com


import example.com.data.user.PostgresUserRepository
import example.com.plugins.*
import example.com.security.hashing.SHA256HashingService
import example.com.security.token.JwtTokenService
import example.com.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main(args: Array<String>) {
    EngineMain.main(args)
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(Authentication)

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresInt = 1000L * 60L * 60L * 24L,
        secret = environment.config.property("jwt.secret").getString(),
    )
    val hashingService = SHA256HashingService()

    configureSecurity(tokenConfig)
    configureSerialization()
    configureDatabases(environment.config)
    configureRouting(
        userRepository = PostgresUserRepository(),
        hashingService = hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig
    )
}
