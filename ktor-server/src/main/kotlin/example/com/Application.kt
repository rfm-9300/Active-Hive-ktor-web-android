package example.com

import example.com.plugins.*
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
    embeddedServer(
        Netty,
        port = 8080, // This is the port on which Ktor is listening
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}
object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        // Load the .env file
        val dotenv = Dotenv.configure().load()
        val databaseName = dotenv["POSTGRES_DB"] ?: throw IllegalStateException("POSTGRES_DB not found")
        val driverClassName = config.property("storage.driverClassName").getString()
        val jdbcURL = "jdbc:postgresql://db:5432/$databaseName"
        val dbUser = dotenv["POSTGRES_USERNAME"] ?: throw IllegalStateException("POSTGRES_USERNAME not found")
        val dbPassword = dotenv["POSTGRES_PASSWORD"] ?: throw IllegalStateException("POSTGRES_PASSWORD not found")


        val flyway = Flyway.configure().dataSource(jdbcURL, dbUser, dbPassword).load()
        flyway.migrate()

        Database.connect(url = jdbcURL, user = dbUser, password = dbPassword, driver = driverClassName)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

fun Application.module() {
    DatabaseFactory.init(environment.config)
    configureSerialization()
    configureDatabases()
    configureRouting()
}
