package example.com.data.db.user

import org.jetbrains.exposed.sql.select

class PostgresUserRepository: UserRepository {
    override suspend fun getUser(username: String): User? = suspendTransaction {
        UserTable
            .select { UserTable.username eq username }
            .singleOrNull()?.let {
                User(
                    username = it[UserTable.username],
                    password = it[UserTable.password],
                    salt = it[UserTable.salt]
                )
            }
    }

    override suspend fun addUser(user: User): Boolean = suspendTransaction {
        try {
            UserDao.new {
                name = user.username
                password = user.password
                salt = user.salt
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