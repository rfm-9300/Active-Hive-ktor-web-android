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


    /****
     * UI Routes
     ****/

    get(Routes.Ui.Auth.SIGNUP) {
        call.respondHtml {
            signupPage()
        }
    }

    get("/login") {
        call.respondHtml {
            loginPage()
        }
    }





    /****
     * Api Routes
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

        val token = tokenService.generateAuthToken(
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

    post(Routes.Api.Auth.SIGNUP){
        Logger.d("Received a request to sign up a new user")
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: return@post respondHelper(success = false, message = "Invalid request", call = call, statusCode = HttpStatusCode.BadRequest)

        val areFieldsEmpty = request.email.isEmpty() || request.password.isEmpty()
        val isPasswordTooShort = request.password.length < PASSWORD_MIN_LENGTH

        if (areFieldsEmpty || isPasswordTooShort) {
            return@post respondHelper(success = false, message = "Empty fields or weak password", call = call, statusCode = HttpStatusCode.BadRequest)
        }

        val user = userRepository.getUser(request.email)
        if (user != null) {
            return@post respondHelper(success = false, message = "User already exists", call = call, statusCode = HttpStatusCode.Conflict)
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val verificationToken = tokenService.generateVerificationToken(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = "1"
            )
        )

        val newUser = User(
            email = request.email,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            verificationToken = verificationToken,
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
}
