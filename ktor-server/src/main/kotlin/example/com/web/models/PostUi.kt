package example.com.web.models

data class PostUi(
    val postId: Int = 0,
    val imgUrl: String = "/resources/default-user-image.webp",
    val userName: String = "User Name",
    val userHandle: String = "@username",
    val title: String = "Post Title",
    val content: String = "Post Content",
    val date: String = "Post Date",
    val likes: Int = 0,
    val comments: Int = 0,
    val views: Int = 0
) {
}