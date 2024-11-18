package cc.atomtech.timetable

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
        public const val STATION_ID = "STATION_ID"
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