package com.example.fifteenfebuaryproject.di

import android.app.Application
import com.example.fifteenfebuaryproject.data.remote.MyApi
import com.example.fifteenfebuaryproject.data.repository.MyRepositoryImpl
import com.example.fifteenfebuaryproject.domain.repository.MyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMyApi(): MyApi {
        return Retrofit.Builder()
            .baseUrl("test_url")
            .build()
            .create(MyApi::class.java)
    }


    @Provides
    @Singleton
    fun provideRepository(
        api: MyApi,
        application: Application,
        @Named("hello1") hello1: String,
    ): MyRepository {
        return MyRepositoryImpl(api = api, application = application)
    }


    @Provides
    @Singleton
    @Named("hello1")
    fun provideString1() = "Hello One"

    @Provides
    @Singleton
    @Named("hello2")
    fun provideString2() = "Hello Two"
}