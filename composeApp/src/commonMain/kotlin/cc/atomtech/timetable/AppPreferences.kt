package cc.atomtech.timetable

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
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

    fun getFavouriteStations(): Flow<Set<String>> {
        return preferences.data.map {
            it[stringSetPreferencesKey(FAVOURITE_STATIONS)] ?: setOf()
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addFavouriteStation(stationId: String) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    val newSet: MutableSet<String> = (it[stringSetPreferencesKey(FAVOURITE_STATIONS)]?.toMutableSet() ?: mutableSetOf<String>())
                    newSet.add(stationId)
                    it[stringSetPreferencesKey(FAVOURITE_STATIONS)] = newSet.toSet()
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }

    suspend fun removeFavouriteStation(stationId: String) {
        return withContext(Dispatchers.IO) {
            try {
                preferences.edit {
                    val newSet: MutableSet<String> = (it[stringSetPreferencesKey(FAVOURITE_STATIONS)]?.toMutableSet() ?: mutableSetOf<String>())
                    newSet.remove(stationId)
                    it[stringSetPreferencesKey(FAVOURITE_STATIONS)] = newSet.toSet()
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }    }
}