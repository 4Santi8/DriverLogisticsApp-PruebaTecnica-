package com.example.driverlogisticsapp.data.remote

import com.example.driverlogisticsapp.domain.model.Delivery
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DeliveryApi {
    @GET(
        value = "4Santi8/7790bc1da9d27a0c5ab36b4f95410092/raw/" +
                "41a96439bbe9c3dc0fb5be94bf76618d444e75a7/listaEntrega.json"
    )
    suspend fun getDeliveryList(): List<Delivery>

    @POST("https://driverlogisticsapp.free.beeceptor.com")
    suspend fun confirmDelivery(@Body confirmation: Delivery): Response<Unit>

}