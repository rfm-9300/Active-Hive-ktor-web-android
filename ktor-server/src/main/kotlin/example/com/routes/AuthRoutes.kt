package example.com.routes

import example.com.data.requests.AuthRequest
import example.com.data.responses.AuthResponse
import example.com.data.db.user.User
import example.com.data.db.user.UserRepository
import example.com.security.hashing.HashingService
import example.com.security.hashing.SaltedHash
import example.com.security.token.TokenClaim
import example.com.security.token.TokenConfig
import example.com.security.token.TokenService
import example.com.web.login.loginPage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val PASSWORD_MIN_LENGTH = 8


fun Route.loginRoutes(
    hashingService: HashingService,
    userRepository: UserRepository,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    get("/login") {
        call.respondHtml {
            loginPage()
        }
    }

    /****
     * sing-up
     ****/

    post("signup"){
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: return@post call.respond(
            HttpStatusCode.BadRequest)

        val areFieldsEmpty = request.username.isEmpty() || request.password.isEmpty()
        val isPasswordTooShort = request.password.length < PASSWORD_MIN_LENGTH
        if (areFieldsEmpty || isPasswordTooShort) {
            return@post call.respond(HttpStatusCode.Conflict)
        }

        val user = userRepository.getUser(request.username)
        if (user != null) {
            return@post call.respond(HttpStatusCode.Conflict)
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val newUser = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        // try to add the user to the database
        val isUserAdded = userRepository.addUser(newUser)

        // if the user was not added, return an internal server error
        if (!isUserAdded) {
            return@post call.respond(HttpStatusCode.InternalServerError)
        }

        call.respond(HttpStatusCode.Created)
    }

    /****
     * login
     ****/

    post("login") {
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)

        val areFieldsEmpty = request.username.isEmpty() || request.password.isEmpty()

        if (areFieldsEmpty) return@post call.respond(HttpStatusCode.Conflict)

        val user = userRepository.getUser(request.username) ?: return@post call.respond(HttpStatusCode.NotFound, "User not found")

        val isPasswordCorrect = hashingService.verifySaltedHash(
            password = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isPasswordCorrect) return@post call.respond(HttpStatusCode.Unauthorized, "Invalid password")

        val token = tokenService.generateToken(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(token = token)
        )
    }

    authenticate {
        get("user") {
            val principal = call.principal<JWTPrincipal>() ?: return@get call.respond(HttpStatusCode.Unauthorized)
            val userId = principal.getClaim("userId", String::class) ?: return@get call.respond(HttpStatusCode.Unauthorized)
            call.respond(
                status = HttpStatusCode.OK,
                message = AuthResponse(token = userId)
            )
        }
    }
}
