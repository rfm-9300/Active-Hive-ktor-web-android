package example.com

import example.com.data.db.user.UserRepositoryImpl
import example.com.di.appModule
import example.com.plugins.*
import example.com.security.hashing.SHA256HashingService
import example.com.security.token.JwtTokenService
import example.com.security.token.TokenConfig
import example.com.services.EmailService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.sessions.*
import io.ktor.server.sse.*
import org.koin.ktor.plugin.Koin
import org.koin.ktor.ext.get
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore

fun main(args: Array<String>) {
        
        EngineMain.main(args)
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

    // Create email service
    val emailService = EmailService(
        smtpHost = environment.config.propertyOrNull("smtp.host")?.getString() ?: "smtp.gmail.com",
        smtpPort = environment.config.propertyOrNull("smtp.port")?.getString()?.toInt() ?: 587,
        username = environment.config.propertyOrNull("smtp.username")?.getString() ?: "abizaria@gmail.com",
        password = environment.config.propertyOrNull("smtp.password")?.getString() ?: "password",
        fromEmail = environment.config.propertyOrNull("smtp.from")?.getString() ?: "noreply@activehive.com",
        isProduction = environment.config.propertyOrNull("smtp.production")?.getString()?.toBoolean() ?: false
    )
    emailService.testSmtpConnection()

    configureSecurity(tokenConfig)
    configureSerialization()
    configureDatabases(environment.config)
    configureRouting(
        userRepository = get(),
        hashingService = hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig,
        sseManager = get(),
        eventRepository = get(),
        postRepository = get(),
        emailService = emailService
    )
}

