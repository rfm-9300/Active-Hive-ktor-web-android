package example.com.data.db.post

import example.com.data.db.user.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostRepositoryImpl: PostRepository {
    override suspend fun addPost(post: PostDao): Boolean {
        return true
    }

    override suspend fun getAllPosts(): List<Post> = suspendTransaction {
         PostDao.all().map { it.toPost() }
    }

    override suspend fun deletePost(postId: Int): Boolean = suspendTransaction {
        PostLikeTable.deleteWhere { PostLikeTable.postId eq postId }
        PostCommentTable.deleteWhere { PostCommentTable.postId eq postId }
        PostDao.findById(postId)?.delete() != null
    }
}