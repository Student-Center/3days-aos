package com.weave.data.di

import com.weave.data.service.ImageApi
import com.weave.network.api.AuthApi
import com.weave.network.api.ChatApi
import com.weave.network.api.CompaniesApi
import com.weave.network.api.LocationsApi
import com.weave.network.api.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun providesAuthService(@UnauthorizedClient client: Retrofit): AuthApi =
        client.create(AuthApi::class.java)

    @Provides
    fun providesCompaniesService(@UnauthorizedClient client: Retrofit): CompaniesApi =
        client.create(CompaniesApi::class.java)

    @Provides
    fun providesLocationsService(@UnauthorizedClient client: Retrofit): LocationsApi =
        client.create(LocationsApi::class.java)

    @Provides
    fun providesUsersService(@AuthorizedClient client: Retrofit): UsersApi =
        client.create(UsersApi::class.java)

    @Provides
    fun providesImageService(@UnauthorizedClient client: Retrofit): ImageApi =
        client.create(ImageApi::class.java)

    @Provides
    fun providesChatService(@AuthorizedClient client: Retrofit): ChatApi =
        client.create(ChatApi::class.java)
}