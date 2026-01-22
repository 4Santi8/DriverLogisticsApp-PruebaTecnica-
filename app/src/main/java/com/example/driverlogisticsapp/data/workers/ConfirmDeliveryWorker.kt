package com.example.driverlogisticsapp.data.workers

import android.content.Context
import android.os.Build
import android.util.Log
import android.util.StatsLog.logStop
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.driverlogisticsapp.data.mapper.toDomain
import com.example.driverlogisticsapp.data.local.database.DeliveryDao
import com.example.driverlogisticsapp.data.remote.DeliveryApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.UUID
import java.util.concurrent.TimeUnit

@HiltWorker
class ConfirmDeliveryWorker @AssistedInject  constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dao: DeliveryDao,
    private val apiService: DeliveryApi
): CoroutineWorker(appContext, workerParams) {

    @RequiresApi(Build.VERSION_CODES.S)
    override suspend fun doWork(): Result{
        val deliveryId = inputData.getLong("deliveryId", 0L)
        Log.i("ID insede worker", deliveryId.toString())

        return try {
            val delivery = dao.getById(deliveryId).toDomain()
            val response = apiService.confirmDelivery(delivery)
            if (response.isSuccessful) {
                Result.success()
            } else {
                Log.e("ConfirmDeliveryWorker", "Error al confirmar la entrega: ${response.code()}")
                Result.retry()
            }
        }catch (e: Exception){
            Log.e("Exception-worker", e.toString())
            Result.retry()
        }finally {
            if (isStopped) {
                val reason = stopReason
                logStop(reason)
                Log.d("WorkerError", "El worker se detuvo por la razón: $reason")
            }
        }

    }
}


fun queueDelivery(context: Context, id: Long): UUID {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val data = workDataOf("deliveryId" to id)

    val request = OneTimeWorkRequestBuilder<ConfirmDeliveryWorker>()
        .addTag(id.toString())
        .setConstraints(constraints)
        .setInputData(data)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            WorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS
        ).build()

    WorkManager
        .getInstance(context)
        .enqueueUniqueWork(
            "sync_deliveries",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            request
        )
    return request.id
}