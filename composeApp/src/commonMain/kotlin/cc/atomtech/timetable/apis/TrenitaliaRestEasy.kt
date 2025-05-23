package cc.atomtech.timetable.apis

import cc.atomtech.timetable.models.trenitalia.CercaTrenoData
import cc.atomtech.timetable.models.trenitalia.RestEasyTrainData
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

/**
 * @since 1.5.0
 */
object TrenitaliaRestEasy {
    private const val baseUrl = "http://www.viaggiatreno.it/infomobilita/resteasy/viaggiatreno/"

    suspend fun searchTrainByNumber(trainNumber: String): CercaTrenoData? {
        val response = HttpClient().get(baseUrl + "cercaNumeroTreno/" + trainNumber)
        val json = Json { ignoreUnknownKeys = true }

        if(response.status != io.ktor.http.HttpStatusCode.OK) return null
        if(response.bodyAsText().isEmpty()) return null
        return json.decodeFromString<CercaTrenoData>(response.bodyAsText())
    }


    suspend fun fetchTrain(train: CercaTrenoData): RestEasyTrainData {
        val response = HttpClient().get(baseUrl + "andamentoTreno/${train.originCode}/${train.number}/${train.departureTimestamp}")

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        return json.decodeFromString<RestEasyTrainData>(response.bodyAsText())
    }
}