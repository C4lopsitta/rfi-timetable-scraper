package cc.atomtech.timetable.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

@Serializable
class Stations(var stations: ArrayList<Station>) {
    companion object {
        fun fromFavourites(favourites: String,
                           stations: Stations): Stations {
            if(favourites.isEmpty()) return Stations(arrayListOf())
            val favouriteStationIds: ArrayList<Int> = arrayListOf()
            favourites.split(";").forEach { favouriteStationIds.add(it.toInt()) }

            val favouriteStations: ArrayList<Station> = arrayListOf()
            favouriteStationIds.forEach { stationId ->
                val station = stations.stations.filter { it.id == stationId }[0]
                favouriteStations.add(station)
            }

            return Stations(favouriteStations)
        }

        fun fromJson(json: String): ArrayList<Station> {
            return Json.decodeFromString<Stations>(json).stations
        }
    }

    fun search(query: String): List<Station> {
        if (query.isEmpty() && stations.isNotEmpty()) {
            return stations.subList(0, 10)
        }
        return stations.filter { it.name.contains(query, ignoreCase = true) }
    }

    fun searchById(query: Int): Station? {
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
