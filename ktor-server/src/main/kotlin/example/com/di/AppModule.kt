package example.com.di
import example.com.data.db.event.EventRepository
import example.com.data.db.event.EventRepositoryImpl
import example.com.data.utils.SseManager
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single { SseManager() }
    singleOf(::EventRepositoryImpl) { bind<EventRepository>() }
}