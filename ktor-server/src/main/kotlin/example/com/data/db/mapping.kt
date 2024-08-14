package example.com.data.db

import example.com.data.user.User
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserTable: IntIdTable("user") {
    val name = varchar("username", 128)
    val password = varchar("password", 256)
    val salt = varchar("salt", 256)
}

class UserDao(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<UserDao>(UserTable)

    var name by UserTable.name
    var password by UserTable.password
    var salt by UserTable.salt
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun UserDao.toUser() = User(name, password, salt, id.value)