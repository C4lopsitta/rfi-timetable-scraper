package cc.atomtech.timetable

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class AppPreferences(
    private val preferences: DataStore<Preferences>
) {
    companion object {
        private const val STATION_ID = "STATION_ID"
        private const val FAVOURITE_STATIONS = "FAVOURITE_STATIONS"
        private const val STATION_LIST_JSON = "STATION_LIST_JSON"
        private const val RELOAD_DELAY = "RELOAD_DELAY"
        private const val USE_NEW_UI = "USE_NEW_UI"
        private const val PRELOAD_NOTICES = "PRELOAD_NOTICES"
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

    fun getUseNewUi(): Flow<Boolean> {
        return preferences.data.map {
            it[booleanPreferencesKey(USE_NEW_UI)] ?: false
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setUseNewUi(value: Boolean) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[booleanPreferencesKey(USE_NEW_UI)] = value
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

    fun getFavouriteStations(): Flow<String> {
        return preferences.data.map {
            it[stringPreferencesKey(FAVOURITE_STATIONS)] ?: ""
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setFavouriteStations(favourites: String) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    it[stringPreferencesKey(FAVOURITE_STATIONS)] = favourites
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }
}