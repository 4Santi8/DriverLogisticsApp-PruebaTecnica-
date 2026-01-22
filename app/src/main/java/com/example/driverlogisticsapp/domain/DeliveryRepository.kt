package com.example.driverlogisticsapp.domain

import com.example.driverlogisticsapp.domain.model.Delivery
import kotlinx.coroutines.flow.Flow

interface DeliveryRepository {
    suspend fun fetchDeliveryList()

    fun updateStatus(id: Long, status: Boolean)

    fun getAll(): List<Delivery>

    fun getAllFlow(): Flow<List<Delivery>>
}