package rfm.biblequizz.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rfm.biblequizz.data.local.Roomdb
import rfm.biblequizz.data.remote.LoginApi
import rfm.biblequizz.data.remote.LoginClient
import rfm.biblequizz.data.repository.LoginRepositoryImpl
import rfm.biblequizz.domain.repository.LoginRepository
import rfm.biblequizz.domain.usecase.LoginUseCase
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
    fun provideLoginUseCase(loginApi: LoginApi, loginRepository: LoginRepository): LoginUseCase {
        return LoginUseCase(loginRepository = loginRepository)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(loginApi: LoginApi, db: Roomdb): LoginRepository {
        return LoginRepositoryImpl(api = loginApi, db = db)
    }
}