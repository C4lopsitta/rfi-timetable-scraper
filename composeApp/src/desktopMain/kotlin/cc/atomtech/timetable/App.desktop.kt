package cc.atomtech.timetable

import cc.atomtech.timetable.enumerations.Platform

actual val platform: Platform
    get() = Platform.DESKTOP_JVM

actual fun toggleStrikesNotificationService(
    isEnabled: Boolean,
    runningHour: Int
) {
}

actual fun debugRunStrikesNotificationService() {
}

actual object AppVersion {
    actual val versionCode: Int
        get() = TODO("Not yet implemented")
    actual val versionName: String
        get() = TODO("Not yet implemented")
}