package com.ramadhan.mysayur.common.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ramadhan.mysayur.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CleanUpWorkers(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) ,KoinComponent{


    private val locationUseCase: com.ramadhan.mysayur.core.domain.usecase.LocationUseCase by inject()

    override fun doWork(): Result {
        Log.d("CleanUpWorkers", "doWork at: " + System.currentTimeMillis())
        locationUseCase.deleteOldData()
        createNotificationChannel()
        showNotification()
        return Result.success()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Location Cleanup Channel"
            val descriptionText = "Notifications for daily location data cleanup"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CLEANUP_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, "CLEANUP_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_android)
                .setContentTitle("Data Lokasi Dihapus")
                .setContentText("Data lokasi Anda telah dihapus pada pukul 10 malam.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }
}

fun scheduleCleanUp(context: Context) {
    val currentTime = System.currentTimeMillis()
    val delay = calculateDelayUntil10PM(currentTime)


    val cleanUpWorker = OneTimeWorkRequestBuilder<CleanUpWorkers>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .addTag("Daily Clean Up")
        .build()
    WorkManager.getInstance(context)
        .enqueueUniqueWork("Daily Clean Up", ExistingWorkPolicy.REPLACE, cleanUpWorker)
}

private fun calculateDelayUntil10PM(currentTime: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTime
    calendar.set(Calendar.HOUR_OF_DAY, 22)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)


    Log.d("DEBUG", "Scheduled Time: " + calendar.time)
    Log.d("DEBUG", "Current Time: " + Calendar.getInstance().time)

    if (currentTime > calendar.timeInMillis) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return calendar.timeInMillis - currentTime
}