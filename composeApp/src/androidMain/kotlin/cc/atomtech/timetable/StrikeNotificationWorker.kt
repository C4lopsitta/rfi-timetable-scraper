package cc.atomtech.timetable

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cc.atomtech.timetable.models.TrenitaliaEventDetails
import cc.atomtech.timetable.models.TrenitaliaInfo
import cc.atomtech.timetable.scrapers.TrenitaliaScraper

class StrikeNotificationWorker (
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    val notificationChannel = "strikes_info"


    override suspend fun doWork(): Result {
        println("Running Worker for Strikes Notification Service")

        val trenitaliaNotices: TrenitaliaInfo
        val notificationManager: NotificationManager

        try {
            trenitaliaNotices = TrenitaliaScraper.scrapePassengerInformation()
            notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        } catch (ex: Exception) {
            println("Notification fetching has failed!\n${ex.printStackTrace()}")
            return Result.retry()
        }

        trenitaliaNotices.irregularTrafficEvents.forEachIndexed { index, trenitaliaEventDetails ->
            if(trenitaliaEventDetails.title.contains("sciopero", ignoreCase = true))
                sendStrikeNotification(
                    trenitaliaEventDetails,
                    index,
                    notificationManager
                )
        }

        return Result.success()
    }

    private fun sendStrikeNotification(
        eventDetail: TrenitaliaEventDetails,
        sequential: Int,
        notificationManager: NotificationManager
    ) {
        val notificationId = 1000 + sequential;

        val channel = NotificationChannel(
            notificationChannel,
            applicationContext.getString(R.string.strikes_notification_channel),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val openAppIntent = Intent(applicationContext, MainActivity::class.java)
        val openAppPendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(applicationContext, notificationChannel)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(applicationContext.getString(R.string.strikes_notification_title))
            .setContentText(eventDetail.title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_launcher_foreground, applicationContext.getString(R.string.open_app), openAppPendingIntent)
            .setAutoCancel(true)
            .setSilent(true)

        eventDetail.link?.let {
            val openLinkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(eventDetail.link))
            val openLinkPendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                openLinkIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            notificationBuilder.addAction(R.drawable.ic_launcher_foreground, applicationContext.getString(R.string.open_in_browser), openLinkPendingIntent)
        }


        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}