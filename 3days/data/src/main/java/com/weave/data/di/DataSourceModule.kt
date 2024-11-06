package com.weave.data.di

import com.weave.data.datasource.AuthRemoteDataSource
import com.weave.data.datasource.AuthRemoteDataSourceImpl
import com.weave.data.datasource.CompanyRemoteDataSource
import com.weave.data.datasource.CompanyRemoteDataSourceImpl
import com.weave.data.datasource.LocationRemoteDataSource
import com.weave.data.datasource.LocationRemoteDataSourceImpl
import com.weave.data.datasource.UserRemoteDataSource
import com.weave.data.datasource.UserRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsAuthDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsCompanyDataSource(impl: CompanyRemoteDataSourceImpl): CompanyRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsLocationDataSource(impl: LocationRemoteDataSourceImpl): LocationRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsUserDataSource(impl: UserRemoteDataSourceImpl): UserRemoteDataSource
}