package example.com.data.db.post

import example.com.data.db.user.suspendTransaction

class PostRepositoryImpl: PostRepository {
    override suspend fun addPost(post: PostDao): Boolean {
        return true
    }

    override suspend fun getAllPosts(): List<Post> = suspendTransaction {
         PostDao.all().map { it.toPost() }
    }
}