package cc.atomtech.timetable

expect object StringRes {
    fun get(id: String, quantity: Int): String
    fun get(id: String): String
    fun format(id: String, vararg formatArgs: Any): String
}
