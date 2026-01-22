package com.example.driverlogisticsapp.data.di

import com.example.driverlogisticsapp.data.repository.DeliveryRepositoryImpl
import com.example.driverlogisticsapp.domain.DeliveryRepository
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
    abstract fun bindDeliveryRepository(
        impl: DeliveryRepositoryImpl
    ): DeliveryRepository

}