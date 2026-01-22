package com.example.driverlogisticsapp.domain.model

data class Delivery(
    val id: Long,
    val name: String,
    val address: String,
    val status: Boolean = false
)