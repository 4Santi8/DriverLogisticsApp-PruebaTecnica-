package com.example.driverlogisticsapp.network

import com.example.driverlogisticsapp.database.DeliveryEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DeliveryApi {
    @GET(
        value = "4Santi8/7790bc1da9d27a0c5ab36b4f95410092/raw/" +
                "41a96439bbe9c3dc0fb5be94bf76618d444e75a7/listaEntrega.json"
    )
    suspend fun getDeliveryList(): List<DeliveryEntity>

    @POST("https://webhook.site/8d68508b-74e3-48cf-8eff-bc9e0448e0b6")
    suspend fun confirmDelivery(@Body confirmation: DeliveryEntity): Response<Unit>

}