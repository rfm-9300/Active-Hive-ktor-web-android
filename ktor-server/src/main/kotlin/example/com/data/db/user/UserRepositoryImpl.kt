package example.com.data.db.user

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepositoryImpl: UserRepository {
    override suspend fun getUser(email: String): User? = suspendTransaction {
        UserTable
            .select { UserTable.email eq email }
            .singleOrNull()?.let {
                User(
                    id = it[UserTable.id].value,
                    email = it[UserTable.email],
                    password = it[UserTable.password],
                    salt = it[UserTable.salt]
                )
            }
    }

    override suspend fun getUserById(userId: Int): User? = suspendTransaction {
        UserTable
            .select { UserTable.id eq userId }
            .singleOrNull()?.let {
                User(
                    email = it[UserTable.email],
                    password = it[UserTable.password],
                    salt = it[UserTable.salt],
                    verified = it[UserTable.verified],
                    verificationToken = it[UserTable.verificationToken],
                )
            }
    }

    override suspend fun addUser(user: User): Boolean = suspendTransaction {
        try {
            UserTable.insert {
                it[email] = user.email
                it[password] = user.password
                it[salt] = user.salt
                it[verified] = user.verified
                it[verificationToken] = user.verificationToken
                it[createdAt] = user.createdAt
            }
            UserProfilesTable.insert {
                it[userId] = UserTable.select { UserTable.email eq user.email }.single()[UserTable.id]
                it[firstName] = user.profile?.firstName ?: ""
                it[lastName] = user.profile?.lastName ?: ""
                it[email] = user.profile?.email ?: ""
                it[phone] = user.profile?.phone ?: ""
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUserProfile(userId: Int): UserProfile = suspendTransaction {
        UserProfilesTable
            .select { UserProfilesTable.userId eq userId }
            .single().let {
                UserProfile(
                    userId = it[UserProfilesTable.userId].value,
                    firstName = it[UserProfilesTable.firstName],
                    lastName = it[UserProfilesTable.lastName],
                    email = it[UserProfilesTable.email],
                    phone = it[UserProfilesTable.phone]
                )
            }
    }

}