package example.com

import example.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun main(args: Array<String>) {
    EngineMain.main(args)
    embeddedServer(
        Netty,
        port = 8080, // This is the port on which Ktor is listening
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}
object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        println("test version")
        // Load the .env file
        val databaseName = config.property("storage.name").getString()
        val host = config.property("storage.host").getString()
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://$host:5432/$databaseName"
        val dbUser = config.property("storage.user").getString()
        val dbPassword = config.property("storage.password").getString()


        val flyway = Flyway.configure().dataSource(jdbcURL, dbUser, dbPassword).load()
        flyway.baseline()
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
