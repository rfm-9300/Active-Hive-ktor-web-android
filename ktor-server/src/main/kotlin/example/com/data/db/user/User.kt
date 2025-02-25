package example.com.data.db.user

import example.com.data.db.event.Event
import example.com.data.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val password: String,
    val salt: String,
    val verified: Boolean = false,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val verificationToken: String? = null,
    val profile: UserProfile? = null
)

@Serializable
data class UserProfile(
    val id: Int? = null,
    val userId : Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String,
    val phone: String = "",
    @Serializable(with = LocalDateTimeSerializer::class)
    val joinedAt: LocalDateTime? = null,
    val hostedEvents : List<Event> = emptyList(),
    val attendedEvents : List<Event> = emptyList(),
    val profileImagePath: String = ""
)

object UserTable : IntIdTable("user") {
    val email = varchar("email", 128)
    val password = varchar("password", 256)
    val salt = varchar("salt", 256)
    val verified = bool("verified").default(false)
    val createdAt = datetime("created_at")
    val verificationToken = varchar("verification_token", 256).nullable()
}

// UserProfilesTable now extends IntIdTable
object UserProfilesTable : IntIdTable("user_profile") {
    val userId = reference("user_id", UserTable).uniqueIndex()
    val firstName = varchar("first_name", 128)
    val lastName = varchar("last_name", 128)
    val email = varchar("email", 128)
    val phone = varchar("phone", 18)
    val joinedAt = datetime("joined_at").default(LocalDateTime.now())
    val imagePath = varchar("image_path", 100)
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

fun UserDao.toUser() = UserProfile(
    id = profile.id.value,
    firstName = profile.firstName,
    lastName = profile.lastName,
    email = profile.email,
    phone = profile.phone
)

fun ResultRow.toUser() = User(
    id = this[UserTable.id].value,
    email = this[UserTable.email],
    password = this[UserTable.password],
    salt = this[UserTable.salt],
    verified = this[UserTable.verified],
    createdAt = this[UserTable.createdAt],
    verificationToken = this[UserTable.verificationToken]
)

fun ResultRow.toUserProfile() = UserProfile(
    id = this[UserProfilesTable.id].value,
    userId = this[UserProfilesTable.userId].value,
    firstName = this[UserProfilesTable.firstName],
    lastName = this[UserProfilesTable.lastName],
    email = this[UserProfilesTable.email],
    phone = this[UserProfilesTable.phone],
    joinedAt = this[UserProfilesTable.joinedAt],
    profileImagePath = this[UserProfilesTable.imagePath]
)