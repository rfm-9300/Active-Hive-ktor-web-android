package example.com.data.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LikeEventManager {
    private val _likeEvents = MutableSharedFlow<LikeEvent>()
    val likeEvents = _likeEvents.asSharedFlow()

    private val _eventDeleted = MutableSharedFlow<Int>()
    val eventDeleted = _eventDeleted.asSharedFlow()

    suspend fun emitLike(postId: Int, likesCount: Int) {
        _likeEvents.emit(LikeEvent(postId, likesCount))
    }

    suspend fun emitDeleteEvent(eventId: Int) {
        _eventDeleted.emit(eventId)
    }
}

data class LikeEvent(val postId: Int, val likesCount: Int)