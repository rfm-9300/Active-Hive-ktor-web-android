package example.com.data.db.user

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

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
                it[joinedAt] = user.profile?.joinedAt ?: LocalDateTime.now()
                it[imagePath] = user.profile?.profileImagePath ?: "profile"
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUserProfile(userId: Int): UserProfile? = suspendTransaction {
        try {
            val userProfile = UserProfilesTable.select { UserProfilesTable.userId eq userId }.single().toUserProfile()
            userProfile
        } catch (e: Exception) {
            null
        }

    }

    override suspend fun updateUserProfile(userProfile: UserProfile): Boolean = suspendTransaction {
        try {
            UserProfilesTable.update({ UserProfilesTable.userId eq userProfile.userId }) {
                it[firstName] = userProfile.firstName
                it[lastName] = userProfile.lastName
                it[email] = userProfile.email
                it[phone] = userProfile.phone
                it[imagePath] = userProfile.profileImagePath
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}