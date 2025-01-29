package example.com.web.pages.homePage.homeTab

import example.com.data.db.post.Post
import example.com.data.db.post.PostRepositoryImpl
import example.com.web.components.layout.layout
import example.com.web.components.post.post
import example.com.web.models.PostUi
import kotlinx.coroutines.*
import kotlinx.html.*

fun HtmlBlockTag.homeTab() {
    // Run blocking to get posts synchronously
    val posts = runBlocking {
        try {
            PostRepositoryImpl().getAllPosts().map { post ->
                PostUi(
                    postId = post.id,
                    userName = post.userName,
                    title = post.title,
                    content = post.content,
                    date = post.date,
                    likes = post.likes
                )
            }
        } catch (e: Exception) {
            println("Error fetching posts: ${e.message}")
            emptyList()
        }
    }

    div(classes = "w-[70%] py-4") {
        posts.forEach { post ->
            post(post)
        }
        script(src = "/resources/js/post.js") {}
    }

}


