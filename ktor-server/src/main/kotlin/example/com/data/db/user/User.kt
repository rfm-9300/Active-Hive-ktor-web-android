package example.com.data.db.user

import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val password: String,
    val salt: String
)

object UserTable: IntIdTable("user") {
    val name = varchar("username", 128)
    val password = varchar("password", 256)
    val salt = varchar("salt", 256)
}

object UserProfilesTable: Table("user_profile") {
    val userId = reference("user_id", UserTable)
    val firstName = varchar("first_name", 128)
    val lastName = varchar("last_name", 128)
    val email = varchar("email", 128)
    val phone = varchar("phone", 18)
}

class UserDao(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<UserDao>(UserTable)

    var name by UserTable.name
    var password by UserTable.password
    var salt by UserTable.salt
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun UserDao.toUser() = User(
    id = id.value,
    username = name,
    password =  password,
    salt = salt
)