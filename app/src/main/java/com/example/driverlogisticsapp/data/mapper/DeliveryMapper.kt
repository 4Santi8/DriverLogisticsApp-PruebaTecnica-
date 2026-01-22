package com.example.driverlogisticsapp.data.mapper

import com.example.driverlogisticsapp.data.local.database.DeliveryEntity
import com.example.driverlogisticsapp.domain.model.Delivery


fun DeliveryEntity.toDomain(): Delivery {
    return Delivery(
        id = id,
        name = name,
        address = address,
        status = status
    )
}

fun Delivery.toEntity(): DeliveryEntity {
    return DeliveryEntity(
        id = id,
        name = name,
        address = address,
        status = status
    )
}