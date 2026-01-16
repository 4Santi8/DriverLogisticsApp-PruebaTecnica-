package com.example.driverlogisticsapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryDao{
    @Query("SELECT * FROM delivery ORDER BY id DESC")
    fun getAllFlow(): Flow<List<DeliveryEntity>>

    @Query("SELECT * FROM delivery ORDER BY id DESC")
    fun getAll(): List<DeliveryEntity>

    @Query("SELECT * FROM delivery WHERE id = :id")
    fun getById(id: Long): DeliveryEntity

    @Query("UPDATE delivery SET status = :status WHERE id = :id")
    fun updateStatus(id: Long, status: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg deliveries: DeliveryEntity)

}