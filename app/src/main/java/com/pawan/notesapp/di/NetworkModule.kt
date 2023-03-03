package com.pawan.notesapp.di

import com.pawan.notesapp.api.AuthIntersepter
import com.pawan.notesapp.api.NotesApi
import com.pawan.notesapp.api.UserAPI
import com.pawan.notesapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder{

        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authIntersepter: AuthIntersepter) : OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authIntersepter).build()
    }

    @Singleton
    @Provides
    fun provideUserAPI(retrofitBuilder: Retrofit.Builder) : UserAPI{
        return  retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideNoteAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient) : NotesApi{
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(NotesApi::class.java)
    }

}