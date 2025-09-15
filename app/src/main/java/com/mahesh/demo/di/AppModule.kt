package com.mahesh.demo.di

import com.mahesh.demo.data.remote.datasources.HoldingsRemoteDataSource
import com.mahesh.demo.data.remote.datasources.HoldingsRemoteDataSourceImpl
import com.mahesh.demo.data.repositories.HoldingsRepositoryImpl
import com.mahesh.demo.domain.repositories.HoldingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideHoldingsRepository(impl: HoldingsRepositoryImpl): HoldingsRepository

    @Binds
    abstract fun provideHoldingsRemoteDataSource(impl: HoldingsRemoteDataSourceImpl): HoldingsRemoteDataSource
}