package example.com.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.io.files.FileNotFoundException
import java.io.File

fun Route.configureJsProcessing() {
    get("/js/{filename}") {
        val directory = "files/js/main"
        try {
            // Get the filename from the request
            val filename = call.parameters["filename"] ?: throw IllegalArgumentException("Filename not provided")
            val files = File(directory).listFiles()?.toList()?.filter { it.name.endsWith(".js") } ?: emptyList()
            val jsFile = files.find { it.name == filename } ?: throw FileNotFoundException("File not found: $filename")

            // Read the JS file from your resources directory
            val jsContent = jsFile.readText()

            // Replace placeholders with actual values
            val processedContent = jsContent.replace(
                mapOf(
                    "%%API_CREATE_EVENT%%" to Routes.API.Event.CREATE
                )
            )
            call.respondText(
                contentType = ContentType.Application.JavaScript,
                text = processedContent
            )
        } catch (e: FileNotFoundException) {
            call.respond(HttpStatusCode.NotFound)
        } catch (e: Exception) {
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