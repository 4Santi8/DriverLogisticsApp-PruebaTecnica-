package com.example.driverlogisticsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [DeliveryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DeliveryDao



}