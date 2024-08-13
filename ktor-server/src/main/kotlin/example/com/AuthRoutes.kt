package example.com

import example.com.data.requests.AuthRequest
import example.com.data.user.User
import example.com.data.user.UserRepository
import example.com.security.hashing.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val PASSWORD_MIN_LENGTH = 8

fun Route.singUp(
    hashingService: HashingService,
    userRepository: UserRepository
    ) {
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
    }

fun Route.login(
    hashingService: HashingService,
    userRepository: UserRepository
) {
    post("login") {
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: return@post call.respond(
            HttpStatusCode.BadRequest)

        val areFieldsEmpty = request.username.isEmpty() || request.password.isEmpty()
        if (areFieldsEmpty) {
            return@post call.respond(HttpStatusCode.Conflict)
        }

        val user = userRepository.getUser(request.username) ?: return@post call.respond(HttpStatusCode.NotFound)
        val saltedHash = hashingService.generateSaltedHash(request.password)

        val isPasswordCorrect = hashingService.verifySaltedHash(request.password, saltedHash)
        if (!isPasswordCorrect) {
            return@post call.respond(HttpStatusCode.Unauthorized)
        }

        call.respond(HttpStatusCode.OK)
    }
}