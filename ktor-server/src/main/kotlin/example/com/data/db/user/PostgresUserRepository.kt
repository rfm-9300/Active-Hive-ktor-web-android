package example.com.data.db.user

import example.com.data.db.user.UserTable.salt
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PostgresUserRepository: UserRepository {
    override suspend fun getUser(email: String): User? = suspendTransaction {
        UserTable
            .select { UserTable.email eq email }
            .singleOrNull()?.let {
                User(
                    email = it[UserTable.email],
                    password = it[UserTable.password],
                    salt = it[UserTable.salt]
                )
            }
    }

    override suspend fun addUser(user: User): Boolean = suspendTransaction {
        try {
            UserTable.insert {
                it[email] = user.email
                it[password] = user.password
                it[salt] = user.salt
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

    override suspend fun getUserProfile(userId: Int): Int {
        return 1
    }

}