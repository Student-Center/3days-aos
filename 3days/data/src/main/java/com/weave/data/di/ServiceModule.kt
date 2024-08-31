package com.weave.data.di

import com.weave.network.api.AuthenticationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun providesAuthService(client: Retrofit): AuthenticationApi =
        client.create(AuthenticationApi::class.java)
}