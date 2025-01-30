package example.com.routes

object Routes {
    object UI {
        object Event {
            const val LIST = "/events"
            const val CREATE = "/events/create"
            const val DETAILS = "/events/{eventId}"
            const val EDIT = "/events/{eventId}/edit"
        }
    }

    object API {
        object Event {
            const val CREATE = "/api/events"
            const val GET = "/api/events/{id}"
            const val UPDATE = "/api/events/{id}"
            const val DELETE = "/api/events/{id}"
            const val LIST = "/api/events"
        }
    }
}

