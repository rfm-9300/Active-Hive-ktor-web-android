package example.com

import example.com.data.db.user.UserRepositoryImpl
import example.com.di.appModule
import example.com.plugins.*
import example.com.security.hashing.SHA256HashingService
import example.com.security.token.JwtTokenService
import example.com.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.sessions.*
import io.ktor.server.sse.*
import org.koin.ktor.plugin.Koin
import org.koin.ktor.ext.get


fun main(args: Array<String>) {
    EngineMain.main(args)
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
        watchPaths = listOf("classes", "resources")
    ).start(wait = true)
}

fun Application.module() {
    install(Koin){
        modules(appModule)
    }
    install(Sessions)
    install(SSE)
    install(Authentication)

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresInt = 1000L * 60L * 60L * 24L,
        secret = environment.config.property("jwt.secret").getString()
    )
    val hashingService = SHA256HashingService()

    configureSecurity(tokenConfig)
    configureSerialization()
    configureDatabases(environment.config)
    configureRouting(
        userRepository = UserRepositoryImpl(),
        hashingService = hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig,
        sseManager = get(),
        eventRepository = get(),
        postRepository = get()
    )
}
