package cc.atomtech.timetable.models

class Stations(var stations: List<Station>) {
    fun search(query: String): List<Station> {
        if (query.isEmpty() && stations.isNotEmpty()) {
            return stations.subList(0, 10)
        }
        return stations.filter { it.name.contains(query, ignoreCase = true) }
    }
}
