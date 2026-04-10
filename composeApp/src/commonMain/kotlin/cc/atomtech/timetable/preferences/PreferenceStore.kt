package cc.atomtech.timetable.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import cc.atomtech.timetable.enumerations.ui.TrainRowDetailLevel
import okio.Path.Companion.toPath


fun createDatastore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

expect fun SetPreferences(prefs: DataStore<Preferences>)


object PreferenceStore {
    private const val LAST_APP_VERSION = "LAST_APP_VERSION"
    private const val STATION_ID = "STATION_ID"
    private const val ALLOW_STORAGE_STATIONS = "ALLOW_STORAGE_STATIONS"
    private const val STATION_LIST_JSON = "STATION_LIST_JSON"
    private const val RELOAD_INTERVAL = "RELOAD_DELAY"
    private const val PRELOAD_NOTICES = "PRELOAD_NOTICES"
    private const val TRAIN_ROW_DETAIL_LEVEL = "train_row_detail_level"
    private const val USE_STRIKES_NOTIFICATION_SERVICE = "use_strikes_notification_service"
    private const val STRIKES_NOTIFICATION_TIME = "time_strikes_notification_service"

    private const val DEFAULT_STATION_ID = 1728; // Milano Centrale


    var preferences: DataStore<Preferences>? = null

    val preferencesFile = "timetables-prefs.preferences_pb"

    val lastAppVersion: ModernMutablePreference<Int> = ModernMutablePreference(
        initialValue = 0,
        keyer = { intPreferencesKey(LAST_APP_VERSION) },
        preferences = preferences
    )

    val stationId: ModernMutablePreference<Int> = ModernMutablePreference(
        initialValue = DEFAULT_STATION_ID,
        keyer = { intPreferencesKey(STATION_ID) },
        preferences = preferences
    )

    val allowStorageStations: ModernMutablePreference<Boolean> = ModernMutablePreference(
        initialValue = true,
        keyer = { booleanPreferencesKey(ALLOW_STORAGE_STATIONS) },
        preferences = preferences
    )

    val stations: ModernMutablePreference<String> = ModernMutablePreference(
        initialValue = "",
        keyer = { stringPreferencesKey(STATION_LIST_JSON) },
        preferences = preferences
    )

    val reloadInterval: ModernMutablePreference<Int> = ModernMutablePreference(
        initialValue = 5,
        keyer = { intPreferencesKey(RELOAD_INTERVAL) },
        preferences = preferences
    )

    val preloadNotices: ModernMutablePreference<Boolean> = ModernMutablePreference(
        initialValue = true,
        keyer = { booleanPreferencesKey(PRELOAD_NOTICES) },
        preferences = preferences
    )

    val trainRowDetailLevel: TransformerMutablePreference<TrainRowDetailLevel, Int> = TransformerMutablePreference(
        initialValue = TrainRowDetailLevel.COMPACT,
        keyer = { intPreferencesKey(TRAIN_ROW_DETAIL_LEVEL) },
        preferences = preferences,
        storeTransformer = { value ->
            return@TransformerMutablePreference value.toValue();
        },
        readTransformer = { value ->
            return@TransformerMutablePreference TrainRowDetailLevel.fromValue(value)
        }
    )

    val useStrikesNotificationService: ModernMutablePreference<Boolean> = ModernMutablePreference(
        initialValue = false,
        keyer = { booleanPreferencesKey(USE_STRIKES_NOTIFICATION_SERVICE) },
        preferences = preferences
    )

    val strikesNotificationTime: ModernMutablePreference<Int> = ModernMutablePreference(
        initialValue = 0,
        keyer = { intPreferencesKey(STRIKES_NOTIFICATION_TIME) },
        preferences = preferences
    )

}

