package cc.atomtech.timetable.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDatastore(context: Context): DataStore<Preferences> = createDatastore {
    context.filesDir.resolve(PreferenceStore.preferencesFile).absolutePath
}

actual fun SetPreferences(prefs: DataStore<Preferences>) {
    PreferenceStore.preferences = prefs
}
