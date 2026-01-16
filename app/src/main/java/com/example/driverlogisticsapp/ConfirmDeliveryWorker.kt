package com.example.driverlogisticsapp

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.driverlogisticsapp.database.DeliveryDao
import com.example.driverlogisticsapp.network.DeliveryApi
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

    override suspend fun doWork(): Result{
        val deliveryId = inputData.getLong("deliveryId", 0L)

        return try {

            val delivery = dao.getById(deliveryId)
            val response = apiService.confirmDelivery(delivery)
            if (response.isSuccessful) {
                Result.success()
            } else {
                Result.retry()
            }
        }catch (e: Exception){
            Log.i("Exception-worker", e.toString())
            Result.retry()
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
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
        .build()

    WorkManager.getInstance(context).enqueue(request)
    return request.id
}