package example.com.routes

object Routes {

    /**
     * Routes for the UI
     **/

    object Ui {
        object Event {
            const val LIST = "/events"
            const val CREATE = "/events/create"
            const val DETAILS = "/events/{eventId}"
            const val UPDATE = "/events/update/{eventId}"
            const val DELETE = "/events/delete/{eventId}"
        }
        object Home {
            const val HOME = "/home"
            const val PROFILE_MENU = "/profile-menu"
        }
        object Auth {
            const val LOGIN = "/login"
            const val SIGNUP = "/signup"
        }

        object Profile {
            const val ROOT = "/profile"
        }
    }

    /**
     * Routes for the API
     **/

    object Api {
        object Event {
            const val CREATE = "/api/events"
            const val GET = "/api/events/{id}"
            const val UPDATE = "/api/events/update"
            const val DELETE = "/api/events/delete"
            const val LIST = "/api/events"
            const val JOIN_EVENT = "/api/events/join"
        }

        object Post {
            const val CREATE = "/api/posts"
            const val GET = "/api/posts/{id}"
            const val UPDATE = "/api/posts/update"
            const val DELETE = "/api/posts/delete"
            const val LIST = "/api/posts"
        }

        object Auth {
            const val LOGIN = "/api/auth/login"
            const val SIGNUP = "/api/auth/signup"
            const val VERIFY = "/api/auth/verify"
        }
    }

    object DynamicJs{
        const val API_CLIENT = "/js/ApiClient.js"
    }

    object Sse {
        const val SSE_CONNECTION = "/sse"
    }

    object Placeholder {
        val PLACEHOLDERS: Map<String, String> = mapOf(
            "%%API_CREATE_EVENT%%" to Api.Event.CREATE,
            "%%API_GET_EVENT%%" to Api.Event.GET,
            "%%API_UPDATE_EVENT%%" to Api.Event.UPDATE,
            "%%API_DELETE_EVENT%%" to Api.Event.DELETE,
            "%%API_LIST_EVENTS%%" to Api.Event.LIST,
            "%%SSE_CONNECTION%%" to Sse.SSE_CONNECTION,
            "%%API_LOGIN%%" to Api.Auth.LOGIN,
            "%%API_SIGNUP%%" to Api.Auth.SIGNUP,
            "%%API_JOIN_EVENT%%" to Api.Event.JOIN_EVENT,
            "%%PROFILE_MENU%%" to Ui.Home.PROFILE_MENU,
            "%%API_CREATE_POST%%" to Api.Post.CREATE,
            "%%API_GET_POST%%" to Api.Post.GET,
            "%%API_UPDATE_POST%%" to Api.Post.UPDATE,
            "%%API_DELETE_POST%%" to Api.Post.DELETE,
            "%%UI_HOME%%" to Ui.Home.HOME,
            "%%EVENTS_TAB%%" to Ui.Event.LIST,

        )
    }
}

