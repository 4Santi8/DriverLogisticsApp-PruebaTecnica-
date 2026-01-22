package com.example.driverlogisticsapp.data.repository

import com.example.driverlogisticsapp.data.local.database.DeliveryDao
import com.example.driverlogisticsapp.data.local.database.DeliveryEntity
import com.example.driverlogisticsapp.data.mapper.toDomain
import com.example.driverlogisticsapp.data.mapper.toEntity
import com.example.driverlogisticsapp.domain.model.Delivery
import com.example.driverlogisticsapp.data.remote.DeliveryApi
import com.example.driverlogisticsapp.domain.DeliveryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeliveryRepositoryImpl @Inject constructor(
    private val api: DeliveryApi,
    private val dao: DeliveryDao
): DeliveryRepository {
    override suspend fun fetchDeliveryList(){
        delay(2000)
        val deliveryList: List<DeliveryEntity> = api.getDeliveryList().map {
            it.toEntity()
        }
        dao.insertAll(*deliveryList.toTypedArray())
    }


    override fun updateStatus(id: Long, status: Boolean){
        dao.updateStatus(id, status)
    }

    override fun getAll(): List<Delivery> = dao.getAll().map { it.toDomain() }


    override fun getAllFlow(): Flow<List<Delivery>> {
        return dao.getAllFlow().map { it ->
            it.map { it.toDomain() }
        }
    }
}