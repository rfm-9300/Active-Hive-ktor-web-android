package example.com.routes

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LikeEventManager {
    private val _likeEvents = MutableSharedFlow<LikeEvent>()
    val likeEvents = _likeEvents.asSharedFlow()

    suspend fun emitLike(postId: Int, likesCount: Int) {
        _likeEvents.emit(LikeEvent(postId, likesCount))
    }
}

data class LikeEvent(val postId: Int, val likesCount: Int)