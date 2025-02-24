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