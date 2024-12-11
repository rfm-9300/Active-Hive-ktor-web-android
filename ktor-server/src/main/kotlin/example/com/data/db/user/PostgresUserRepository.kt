package example.com.data.db.user

class PostgresUserRepository: UserRepository {
    override suspend fun getUser(username: String): User? = suspendTransaction {
        UserDao.find { UserTable.name eq username }.firstOrNull()?.toUser()
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

}