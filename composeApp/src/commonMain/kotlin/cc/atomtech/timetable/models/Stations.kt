package cc.atomtech.timetable.models

import cc.atomtech.timetable.models.rfi.StationBaseData
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

@Serializable
class Stations(var stations: ArrayList<StationBaseData>) {
    companion object {
        fun fromFavourites(favourites: String,
                           stations: List<StationBaseData>): Stations {
            if(favourites.isEmpty()) return Stations(arrayListOf())
            val favouriteStationIds: ArrayList<Int> = arrayListOf()
            favourites.split(";").forEach { favouriteStationIds.add(it.toInt()) }

            val favouriteStations: ArrayList<StationBaseData> = arrayListOf()
            favouriteStationIds.forEach { stationId ->
                val station = stations.filter { it.id == stationId }[0]
                favouriteStations.add(station)
            }

            return Stations(favouriteStations)
        }

        fun fromJson(json: String): ArrayList<StationBaseData> {
            return Json.decodeFromString<Stations>(json).stations
        }
    }

    fun search(query: String, favouriteStations: List<StationBaseData>? = null): List<StationBaseData> {
        if (query.isEmpty() && stations.isNotEmpty()) {
            return favouriteStations ?: stations.subList(0, 10)
        }
        return stations.filter { it.name.contains(query, ignoreCase = true) }
    }

    fun searchById(query: Int): StationBaseData? {
        return try {
            stations.first {
                it.id == query
            }
        } catch (_: Exception) {
            null
        }
    }

    override fun toString(): String {
        return stations.joinToString(";")
    }

    fun toJson(): String {
        return Json.encodeToString(this)
    }
}
