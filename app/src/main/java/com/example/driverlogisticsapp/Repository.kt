package com.example.driverlogisticsapp

import com.example.driverlogisticsapp.database.DeliveryDao
import com.example.driverlogisticsapp.database.DeliveryEntity
import com.example.driverlogisticsapp.network.DeliveryApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: DeliveryApi,
    private val dao: DeliveryDao
) {
    suspend fun fetchDeliveryList(){
        delay(2000)
        val deliveryList = api.getDeliveryList()
        dao.insertAll(*deliveryList.toTypedArray())
    }


    fun updateStatus(id: Long, status: Boolean){
        dao.updateStatus(id, status)
    }

    fun getAll(): List<DeliveryEntity> = dao.getAll()


    fun getAllFlow(): Flow<List<DeliveryEntity>> = dao.getAllFlow()

}