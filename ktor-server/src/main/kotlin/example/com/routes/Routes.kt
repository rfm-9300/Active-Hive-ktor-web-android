package example.com.routes

object Routes {
    object Ui {
        object Event {
            const val LIST = "/events"
            const val CREATE = "/events/create"
            const val DETAILS = "/events/{eventId}"
            const val EDIT = "/events/edit/{eventId}"
            const val DELETE = "/events/delete/{eventId}"
        }
    }

    object Api {
        object Event {
            const val CREATE = "/api/events"
            const val GET = "/api/events/{id}"
            const val UPDATE = "/api/events/{id}"
            const val DELETE = "/api/events/{id}"
            const val LIST = "/api/events"
        }
    }

    object DynamicJs{
        const val API_CLIENT = "/js/ApiClient.js"
    }

    object Placeholder {
        val PLACEHOLDERS: Map<String, String> = mapOf(
            "%%API_CREATE_EVENT%%" to Api.Event.CREATE,
            "%%API_GET_EVENT%%" to Api.Event.GET,
            "%%API_UPDATE_EVENT%%" to Api.Event.UPDATE,
            "%%API_DELETE_EVENT%%" to Api.Event.DELETE,
            "%%API_LIST_EVENTS%%" to Api.Event.LIST
        )
    }
}

