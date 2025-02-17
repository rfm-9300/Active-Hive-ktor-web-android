package example.com.security

object Roles {
    enum class Role {
        ADMIN,
        USER
    }
    fun returnRole(userId: Int): Role {
        return if (userId == 1) {
            Role.ADMIN
        } else {
            Role.USER
        }
    }
}