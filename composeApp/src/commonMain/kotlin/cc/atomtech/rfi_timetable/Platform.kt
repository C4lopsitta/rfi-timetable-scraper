package cc.atomtech.rfi_timetable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform