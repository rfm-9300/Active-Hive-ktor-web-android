package example.com.data.db.post

interface PostRepository {
    suspend fun addPost(post: PostDao) : Boolean
    suspend fun getAllPosts(): List<Post>
}