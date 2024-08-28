package rfm.biblequizz.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rfm.biblequizz.data.local.Roomdb
import rfm.biblequizz.data.remote.LoginApi
import rfm.biblequizz.data.remote.LoginClient
import rfm.biblequizz.data.remote.QuizzApi
import rfm.biblequizz.data.remote.QuizzClient
import rfm.biblequizz.data.remote.interceptor.MockRequestInterceptor
import rfm.biblequizz.data.repository.LoginRepositoryImpl
import rfm.biblequizz.data.repository.QuestionRepositoryImpl
import rfm.biblequizz.domain.repository.LoginRepository
import rfm.biblequizz.domain.repository.QuestionRepository

import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): Roomdb {
        return Roomdb.build(appContext)
    }

    @Provides
    @Singleton
    fun provideLoginApi(): LoginApi {
        return LoginClient.build()
    }

    @Provides
    @Singleton
    fun provideLoginRepository(loginApi: LoginApi, db: Roomdb): LoginRepository {
        return LoginRepositoryImpl(api = loginApi, db = db)
    }


    @Provides
    @Singleton
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }

    @Provides
    @Singleton
    fun provideQuizApi(mockRequestInterceptor: MockRequestInterceptor): QuizzApi {
        return QuizzClient.build(mockRequestInterceptor)
    }

    @Provides
    @Singleton
    fun provideMockRequestInterceptor(@ApplicationContext appContext: Context): MockRequestInterceptor {
        return MockRequestInterceptor(appContext)
    }

    @Provides
    @Singleton
    fun provideQuestion(quizApi: QuizzApi, db: Roomdb): QuestionRepository {
        return QuestionRepositoryImpl(db = db, quizzApi = quizApi)
    }


}