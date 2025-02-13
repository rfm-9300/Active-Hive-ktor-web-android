package example.com.routes

import com.auth0.jwt.JWT
import example.com.data.requests.AuthRequest
import example.com.data.db.user.User
import example.com.data.db.user.UserProfile
import example.com.data.db.user.UserRepository
import example.com.data.responses.ApiResponse
import example.com.data.responses.ApiResponseData
import example.com.plugins.Logger
import example.com.security.hashing.HashingService
import example.com.security.hashing.SaltedHash
import example.com.security.token.TokenClaim
import example.com.security.token.TokenConfig
import example.com.security.token.TokenService
import example.com.web.pages.auth.loginPage
import example.com.web.pages.auth.signupPage
import io.ktor.http.*
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

    post(Routes.Api.Auth.SIGNUP){
        Logger.d("Received a request to sign up a new user")
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
        Logger.d("Received a request to sign up a new user with email: ${request.email}")
        val areFieldsEmpty = request.email.isEmpty() || request.password.isEmpty()
        val isPasswordTooShort = request.password.length < PASSWORD_MIN_LENGTH
        if (areFieldsEmpty || isPasswordTooShort) {
            return@post call.respond(
                HttpStatusCode.Conflict,
                ApiResponse(
                    success = false,
                    message = "Invalid email or password"
                )
            )
        }

        val user = userRepository.getUser(request.email)
        if (user != null) {
            return@post call.respond(HttpStatusCode.Conflict , ApiResponse(
                success = false,
                message = "User already exists"
            ))
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val newUser = User(
            email = request.email,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            profile = UserProfile( email = request.email)
        )

        // try to add the user to the database
        val isUserAdded = userRepository.addUser(newUser)

        // if the user was not added, return an internal server error
        if (!isUserAdded) {
            return@post call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse(
                    success = false,
                    message = "Failed to add user"
                )
            )
        }

        call.respond(
            HttpStatusCode.Created,
            ApiResponse(
                success = true,
                message = "User added successfully"
            )
        )
    }

    get(Routes.Ui.Auth.SIGNUP) {
        call.respondHtml {
            signupPage()
        }
    }

    /****
     * login
     ****/

    post(Routes.Api.Auth.LOGIN) {
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: return@post respondHelper(success = false, message = "Invalid request", call = call)

        val areFieldsEmpty = request.email.isEmpty() || request.password.isEmpty()

        if (areFieldsEmpty) return@post respondHelper(success = false, message = "Email or Password empty", call = call)

        val user = userRepository.getUser(request.email) ?: return@post respondHelper(success = false, message = "User not found", call = call, statusCode = HttpStatusCode.NotFound)

        val isPasswordCorrect = hashingService.verifySaltedHash(
            password = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isPasswordCorrect) return@post respondHelper(success = false, message = "Invalid password", call = call, statusCode = HttpStatusCode.Unauthorized)

        val token = tokenService.generateToken(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        Logger.d("Generated token: $token")
        val decodedJWT = JWT.decode(token)
        Logger.d("Decoded JWT claims: ${decodedJWT.claims}")
        Logger.d("UserID in token: ${decodedJWT.getClaim("userId").asString()}")

        respondHelper(success = true, message = "User logged in", data = ApiResponseData.AuthResponse(token = token), call = call)
    }

    authenticate {
        get("user") {
            val principal = call.principal<JWTPrincipal>() ?: return@get call.respond(HttpStatusCode.Unauthorized)
            val userId = principal.getClaim("userId", String::class) ?: return@get call.respond(HttpStatusCode.Unauthorized)
            respondHelper(success = true, message = "User found", call = call)
        }
    }
}
