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
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun main(args: Array<String>) {
    // Generate SSL certificates before starting the server
    SSLKeyGenerator.generate()
    
    // Copy the keystore to the exact location where Ktor is looking for it
    try {
        val source = File(SSLKeyGenerator.KEYSTORE_PATH)
        val dest = File("./keystore.jks")
        
        if (source.exists()) {
            println("Copying keystore from ${source.absolutePath} to ${dest.absolutePath}")
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING)
            println("Keystore copied successfully!")
        } else {
            println("Source keystore does not exist at ${source.absolutePath}")
        }
    } catch (e: Exception) {
        println("Error copying keystore: ${e.message}")
        e.printStackTrace()
    }
    
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
        smtpHost = environment.config.propertyOrNull("smtp.host")?.getString() ?: "smtp.example.com",
        smtpPort = environment.config.propertyOrNull("smtp.port")?.getString()?.toInt() ?: 587,
        username = environment.config.propertyOrNull("smtp.username")?.getString() ?: "user",
        password = environment.config.propertyOrNull("smtp.password")?.getString() ?: "password",
        fromEmail = environment.config.propertyOrNull("smtp.from")?.getString() ?: "noreply@activehive.com",
        isProduction = environment.config.propertyOrNull("smtp.production")?.getString()?.toBoolean() ?: false
    )

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
        postRepository = get(),
        emailService = emailService
    )
}
