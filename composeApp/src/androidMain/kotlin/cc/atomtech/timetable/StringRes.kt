package cc.atomtech.timetable

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
actual object StringRes {
    lateinit var context: Context

    @SuppressLint("DiscouragedApi")
    actual fun get(id: String): String {
        val resourceId = context.resources.getIdentifier(id, "string", context.packageName)
        if (resourceId == 0) return id
        return context.getString(resourceId)
    }

    @SuppressLint("DiscouragedApi")
    actual fun get(id: String, quantity: Int): String {
        val resourceId = context.resources.getIdentifier(id, "plurals", context.packageName)
        if (resourceId == 0) return id
        return context.resources.getQuantityString(resourceId, quantity, quantity)
    }

    @SuppressLint("DiscouragedApi")
    actual fun format(id: String, vararg formatArgs: Any): String {
        val resourceId = context.resources.getIdentifier(id, "string", context.packageName)
        if (resourceId == 0) return id
        return context.resources.getString(resourceId, *formatArgs)
    }
}