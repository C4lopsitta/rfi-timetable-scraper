package cc.atomtech.timetable

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import cc.atomtech.timetable.enumerations.ui.TrainRowDetailLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class AppPreferences(
    private val preferences: DataStore<Preferences>
) {
    companion object {
        private const val LAST_APP_VERSION = "LAST_APP_VERSION"
        private const val STATION_ID = "STATION_ID"
        private const val ALLOW_STORAGE_STATIONS = "ALLOW_STORAGE_STATIONS"
        private const val STATION_LIST_JSON = "STATION_LIST_JSON"
        private const val RELOAD_DELAY = "RELOAD_DELAY"
        private const val PRELOAD_NOTICES = "PRELOAD_NOTICES"
        private const val TRAIN_ROW_DETAIL_LEVEL = "train_row_detail_level"
        private const val USE_STRIKES_NOTIFICATION_SERVICE = "use_strikes_notification_service"
        private const val STRIKES_NOTIFICATION_TIME = "time_strikes_notification_service"
    }

    fun getTrainRowDetailLevel(): Flow<TrainRowDetailLevel> {
        return preferences.data.map {
            TrainRowDetailLevel.fromValue((it[intPreferencesKey(TRAIN_ROW_DETAIL_LEVEL)] ?: 0))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setTrainRowDetailLevel(detailLevel: TrainRowDetailLevel) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[intPreferencesKey(TRAIN_ROW_DETAIL_LEVEL)] = detailLevel.toValue()
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }

    fun getLastAppVersion(): Flow<Int> {
        return preferences.data.map {
            it[intPreferencesKey(LAST_APP_VERSION)] ?: 0
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setLastAppVersion(version: Int) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[intPreferencesKey(LAST_APP_VERSION)] = version
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }

    fun getStoreStations(): Flow<Boolean> {
        return preferences.data.map {
            it[booleanPreferencesKey(ALLOW_STORAGE_STATIONS)] ?: true
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setStoreStations(value: Boolean) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[booleanPreferencesKey(ALLOW_STORAGE_STATIONS)] = value
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }

    fun getUseStrikesNotificationService(): Flow<Boolean> {
        return preferences.data.map {
            it[booleanPreferencesKey(USE_STRIKES_NOTIFICATION_SERVICE)] ?: true
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setUseStrikesNotificationService(value: Boolean) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[booleanPreferencesKey(USE_STRIKES_NOTIFICATION_SERVICE)] = value
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }

    fun getStationCache(): Flow<String> {
        return preferences.data.map {
            it[stringPreferencesKey(STATION_LIST_JSON)] ?: ""
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setStationCache(newStationJson: String) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[stringPreferencesKey(STATION_LIST_JSON)] = newStationJson
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }

    fun getPreloadNotices(): Flow<Boolean> {
        return preferences.data.map {
            it[booleanPreferencesKey(PRELOAD_NOTICES)] ?: true
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setPreloadNotices(value: Boolean) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[booleanPreferencesKey(PRELOAD_NOTICES)] = value
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }

    fun getReloadDelay(): Flow<Int> {
        return preferences.data.map {
            it[intPreferencesKey(RELOAD_DELAY)] ?: 1
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setReloadDelay(reloadDelay: Int) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[intPreferencesKey(RELOAD_DELAY)] = reloadDelay
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }

    fun getStationId(): Flow<Int> {
        return preferences.data.map {
            it[intPreferencesKey(STATION_ID)] ?: 1728
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setStationId(stationId: Int) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[intPreferencesKey(STATION_ID)] = stationId
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }
}