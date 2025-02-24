package cc.atomtech.timetable

import android.icu.util.Calendar
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cc.atomtech.timetable.enumerations.Platform
import java.util.concurrent.TimeUnit

actual val platform: Platform
    get() = Platform.ANDROID

actual fun toggleStrikesNotificationService(
    isEnabled: Boolean,
    runningHour: Int
) {
    val context = MainActivity.instance

    if (!isEnabled) {
        // Optionally cancel any existing work if the service is disabled
        WorkManager.getInstance(context).cancelUniqueWork("StrikeNotificationWorker")
        return
    }
//
//    val permission = Manifest.permission.POST_NOTIFICATIONS
//    when {
//        ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> {
//            onGranted()
//        }
//        activity.shouldShowRequestPermissionRationale(permission) -> {
//            // Show a UI explanation (optional) before requesting permission
//            AlertDialog.Builder(activity)
//                .setTitle("Enable Notifications")
//                .setMessage("This app needs notification permission to alert you about important updates.")
//                .setPositiveButton("OK") { _, _ ->
//                    requestPermissionLauncher.launch(permission)
//                }
//                .setNegativeButton("Cancel", null)
//                .show()
//        }
//        else -> {
//            // Directly request permission
//            requestPermissionLauncher.launch(permission)
//        }
//    }

    // Calculate the initial delay until the next occurrence of the specified hour
    val now = Calendar.getInstance()
    val scheduledTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, runningHour)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    // If the scheduled time has already passed today, schedule for tomorrow
    if (now.after(scheduledTime)) {
        scheduledTime.add(Calendar.DAY_OF_MONTH, 1)
    }
    val initialDelay = scheduledTime.timeInMillis - now.timeInMillis

    // Create a PeriodicWorkRequest to run daily
    val workRequest = PeriodicWorkRequestBuilder<StrikeNotificationWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "StrikeNotificationWorker",
        ExistingPeriodicWorkPolicy.UPDATE,
        workRequest
    )

}

actual fun debugRunStrikesNotificationService() {
    val context = MainActivity.instance

    val workRequest = OneTimeWorkRequestBuilder<StrikeNotificationWorker>().build()
    WorkManager.getInstance(context).enqueue(workRequest)
}