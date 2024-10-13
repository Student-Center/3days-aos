package com.weave.data.di

import com.weave.network.api.AuthApi
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
    fun providesAuthService(client: Retrofit): AuthApi =
        client.create(AuthApi::class.java)

    @Provides
    fun providesCompaniesService(client: Retrofit): CompaniesApi =
        client.create(CompaniesApi::class.java)

    @Provides
    fun providesLocationsService(client: Retrofit): LocationsApi =
        client.create(LocationsApi::class.java)

    @Provides
    fun providesUsersService(client: Retrofit): UsersApi =
        client.create(UsersApi::class.java)
}