package example.com.data.db.post

import example.com.data.db.user.UserDao
import example.com.data.db.user.UserTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime

@Serializable
data class Post(
    val id: Int,
    val userName: String,
    val title: String,
    val content: String,
    val date: String,
    val likes: Int = 0,
)

object PostTable: IntIdTable("post") {
    val title = varchar("title", 255)
    val content = text("content")
    val date = datetime("date")
    val userId = reference("user_id", UserTable)
}
object PostLikeTable: Table("post_like") {
    val postId = reference("post_id", PostTable)
    val userId = reference("user_id", UserTable)
    override val primaryKey = PrimaryKey(postId, userId)
}
object PostCommentTable: IntIdTable("post_comment") {
    val postId = reference("post_id", PostTable)
    val userId = reference("user_id", UserTable)
    val content = text("content")
    val date = datetime("date")
}

class PostDao(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<PostDao>(PostTable)

    var title by PostTable.title
    var content by PostTable.content
    var date by PostTable.date
    var userId by UserDao referencedOn PostTable.userId
    var likes by UserDao via PostLikeTable
}

fun PostDao.toPost() = Post(
    id = id.value,
    userName = userId.name,
    title = title,
    content = content,
    date = date.toString(),
    likes = likes.count().toInt()
)
