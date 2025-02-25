package cc.atomtech.timetable.apis

import cc.atomtech.timetable.models.TrenitaliaTrainData
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

object TrenitaliaRestEasy {
    private const val trainSearchUrl = "http://www.viaggiatreno.it/infomobilitamobile/resteasy/viaggiatreno/cercaNumeroTrenoTrenoAutocomplete/"


    suspend fun fetchTrainByNumber(trainNumber: String): List<TrenitaliaTrainData> {
        val response = HttpClient().get(trainSearchUrl + trainNumber)
        val tsv = response.bodyAsText()

        val entries = arrayListOf<TrenitaliaTrainData>()

        tsv.lines().forEach { line ->
            val fields = line.split("|")
            if(fields.size >= 2) {
                entries.add(
                    TrenitaliaTrainData(
                        number = fields[0].split(" - ")[0],
                        originStationId = fields[1].split("-")[1],
                        departureTime = fields[1].split("-")[2]
                    )
                )
            }
        }

        return entries.toList()
    }


    suspend fun searchTrainByNumber() {

    }
}