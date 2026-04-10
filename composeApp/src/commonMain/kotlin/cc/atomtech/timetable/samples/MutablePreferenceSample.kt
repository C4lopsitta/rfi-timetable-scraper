package cc.atomtech.timetable.samples

import androidx.datastore.preferences.core.intPreferencesKey
import cc.atomtech.timetable.preferences.ModernMutablePreference
import cc.atomtech.timetable.preferences.PreferenceStore.preferences

val myMutablePreference = ModernMutablePreference<Int>(
    initialValue = 0,
    keyer = { intPreferencesKey("MY_KEY") },
    preferences = preferences
)


