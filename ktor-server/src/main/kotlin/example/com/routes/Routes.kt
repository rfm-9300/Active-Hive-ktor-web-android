package example.com.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.io.files.FileNotFoundException
import java.io.File

object Routes {
    object UI {
        object Event {
            const val LIST = "/events"
            const val CREATE = "/events/create"
            const val DETAILS = "/events/{eventId}"
            const val EDIT = "/events/{eventId}/edit"
        }
    }

    object API {
        object Event {
            const val CREATE = "/api/events"
            const val GET = "/api/events/{id}"
            const val UPDATE = "/api/events/{id}"
            const val DELETE = "/api/events/{id}"
            const val LIST = "/api/events"
        }
    }
}

// Create a function to handle JS file processing
fun Route.configureJsProcessing() {
    get("/test/resources/js/{filename}") {
        try {
            // Get the filename from the request
            val filename = call.parameters["filename"] ?: throw IllegalArgumentException("Filename not provided")
            println("\u001B[31m[DEBUG] Requested filename: $filename\u001B[0m")

            val directory = "files/js/main"
            println("\u001B[31m[DEBUG] Looking in directory: $directory\u001B[0m")

            val files = File(directory).listFiles()?.toList()?.filter { it.name.endsWith(".js") } ?: emptyList()
            println("\u001B[31m[DEBUG] Found JS files: ${files.map { it.name }}\u001B[0m")
            val jsFile = files.find { it.name == filename } ?: throw FileNotFoundException("File not found: $filename")
            val jsContentText = jsFile.readText()
            println("\u001B[31m[DEBUG] Found file content: ${jsContentText.length}\u001B[0m")

            // Read the JS file from your resources directory
            println("\u001B[31m[DEBUG] Attempting to read from: files/js/main/$filename\u001B[0m")
            val jsContent = jsFile.readText()
                ?: throw FileNotFoundException("File not found: $filename")
            println("\u001B[31m[DEBUG] Successfully read file content. Length: ${jsContent.length}\u001B[0m")

            // Replace placeholders with actual values
            println("\u001B[31m[DEBUG] Replacing placeholder %%API_CREATE_EVENT%% with ${Routes.API.Event.CREATE}\u001B[0m")
            val processedContent = jsContent.replace(
                mapOf(
                    "%%API_CREATE_EVENT%%" to Routes.API.Event.CREATE
                )
            )
            println("\u001B[31m[DEBUG] Content processed. Final length: ${processedContent.length}\u001B[0m")

            call.respondText(
                contentType = ContentType.Application.JavaScript,
                text = processedContent
            )
            println("\u001B[31m[DEBUG] Response sent successfully\u001B[0m")

        } catch (e: FileNotFoundException) {
            println("\u001B[31m[DEBUG] FileNotFoundException: ${e.message}\u001B[0m")
            call.respond(HttpStatusCode.NotFound)
        } catch (e: Exception) {
            println("\u001B[31m[DEBUG] Exception occurred: ${e.message}\u001B[0m")
            println("\u001B[31m[DEBUG] Stack trace: ${e.stackTrace.joinToString("\n")}\u001B[0m")
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}


// Extension function to handle replacements
private fun String.replace(replacements: Map<String, String>): String {
    var result = this
    replacements.forEach { (placeholder, value) ->
        result = result.replace(placeholder, value)
    }
    return result
}