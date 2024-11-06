package com.weave.data.di

import com.weave.data.repository.AuthRepositoryImpl
import com.weave.data.repository.CompanyRepositoryImpl
import com.weave.data.repository.LocationRepositoryImpl
import com.weave.data.repository.UserRepositoryImpl
import com.weave.domain.repository.AuthRepository
import com.weave.domain.repository.CompanyRepository
import com.weave.domain.repository.LocationRepository
import com.weave.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsCompanyRepository(impl: CompanyRepositoryImpl): CompanyRepository

    @Binds
    @Singleton
    abstract fun bindsLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository
}