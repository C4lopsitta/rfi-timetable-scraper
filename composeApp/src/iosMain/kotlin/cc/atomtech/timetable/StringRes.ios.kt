package cc.atomtech.timetable

actual object StringRes {
    actual fun get(id: String, quantity: Int): String {
        return id
    }

    actual fun get(id: String): String {
        return id
    }

    actual fun format(id: String, vararg formatArgs: Any): String {
        return id
    }
}
