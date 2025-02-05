package example.com.data.db.user

import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val password: String,
    val salt: String,
    val profile: UserProfile? = null
)

@Serializable
data class UserProfile(
    val id: Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String,
    val phone: String = ""
)

object UserTable : IntIdTable("user") {
    val email = varchar("email", 128)
    val password = varchar("password", 256)
    val salt = varchar("salt", 256)
}

// UserProfilesTable now extends IntIdTable
object UserProfilesTable : IntIdTable("user_profile") {
    val userId = reference("user_id", UserTable).uniqueIndex()
    val firstName = varchar("first_name", 128)
    val lastName = varchar("last_name", 128)
    val email = varchar("email", 128)
    val phone = varchar("phone", 18)
}

class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(UserTable)

    var name by UserTable.email
    var password by UserTable.password
    var salt by UserTable.salt
    var profile by UserProfileDao referencedOn UserProfilesTable.userId
}

class UserProfileDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserProfileDao>(UserProfilesTable)

    var firstName by UserProfilesTable.firstName
    var lastName by UserProfilesTable.lastName
    var email by UserProfilesTable.email
    var phone by UserProfilesTable.phone
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun UserDao.toUser() = User(
    id = id.value,
    email = name,
    password = password,
    salt = salt,
    profile = UserProfile(
        id = profile.id.value,
        firstName = profile.firstName,
        lastName = profile.lastName,
        email = profile.email,
        phone = profile.phone
    )
)