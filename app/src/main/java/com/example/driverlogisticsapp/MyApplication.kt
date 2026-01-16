package com.example.driverlogisticsapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MyApplication: Application(), Configuration.Provider{
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}