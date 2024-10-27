package com.weave.data.di

import com.weave.data.datasource.AuthRemoteDataSource
import com.weave.data.datasource.AuthRemoteDataSourceImpl
import com.weave.data.datasource.CompanyRemoteDataSource
import com.weave.data.datasource.CompanyRemoteDataSourceImpl
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
}