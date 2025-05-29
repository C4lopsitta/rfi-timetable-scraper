package cc.atomtech.timetable.apis

import cc.atomtech.timetable.models.trenitalia.CercaTrenoData
import cc.atomtech.timetable.models.trenitalia.RestEasyTrainData
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

/** API Handler for Trenitalia's mysterious ViaggiaTreno/RestEasy API.
 *
 * @see [RestEasyTrainData]
 * @see [CercaTrenoData]
 * @since 1.5.0
 * @author Simone Robaldo
 */
object TrenitaliaRestEasy {
    private const val baseUrl = "http://www.viaggiatreno.it/infomobilita/resteasy/viaggiatreno/"

    /** Searches for an individual train by its Running Number.
     *
     * @return [CercaTrenoData] when the trains has been successfully fetched, otherwise `null` will be returned.
     * @author Simone Robaldo
     */
    suspend fun searchTrainByNumber(trainNumber: String): CercaTrenoData? {
        val response = HttpClient().get(baseUrl + "cercaNumeroTreno/" + trainNumber)
        val json = Json { ignoreUnknownKeys = true }

        if(response.status != HttpStatusCode.OK) return null
        if(response.bodyAsText().isEmpty()) return null
        return json.decodeFromString<CercaTrenoData>(response.bodyAsText())
    }

    /** Searches for any available train with the matching number.
     *
     * This API call will fetch any matching train, which means multiple trains with the *same number*
     * might be returned, having a different departure time or origin.
     *
     * @return [List<CercaTrenoData>] a list containing the matching train(s), empty when none are found.
     * @author Simone Robaldo
     */
    suspend fun searchTrainByNumberWithAutocompletion(trainNumber: String): List<CercaTrenoData> {
        val response = HttpClient().get(baseUrl + "cercaNumeroTrenoTrenoAutocomplete/" + trainNumber)
        val tsvResponse = response.bodyAsText()

        if(response.status != HttpStatusCode.OK) return emptyList()
        if(tsvResponse.isEmpty()) return emptyList()

        val trains = arrayListOf<CercaTrenoData>()

        tsvResponse.lines().forEach { line ->
            val fields = line.split('|')

            if(fields.size >= 2) {
                val humanReadableFields = fields[0].split(regex = Regex(" - "))
                val machineReadableFields = fields[1].split("-")

                trains.add(
                    CercaTrenoData(
                        number = humanReadableFields[0],
                        originCode = machineReadableFields[1],
                        originName = humanReadableFields[1],
                        departureDate = humanReadableFields[2],
                        corsa = "",
                        h24 = false,
                        type = "",
                        departureTimestamp = humanReadableFields[2],
                    )
                )
            }
        }

        return trains
    }

    /** Given a [train], it will return the String URL to open in-browser to view the train's status.
     *
     * @return [String] URL
     * @author Simone Robaldo
     */
    fun buildViaggiaTrenoWebUiUrl(train: CercaTrenoData): String =
        "http://www.viaggiatreno.it/infomobilitamobile/pages/cercaTreno/cercaTreno.jsp?treno=${train.number}&origine=${train.originCode}&datapartenza=${train.departureTimestamp}"

    /** Fetches, given a [CercaTrenoData] query result, the entire train's current status as a [RestEasyTrainData].
     *
     * @return A [RestEasyTrainData] instance containing every available parameter.
     * @throws [Exception] when there has been an error in fetching the train data on the API
     * @author Simone Robaldo
     */
    suspend fun fetchTrain(train: CercaTrenoData): RestEasyTrainData {
        val response = HttpClient().get(baseUrl + "andamentoTreno/${train.originCode}/${train.number}/${train.departureTimestamp}")

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        if(response.status == HttpStatusCode.OK)
            return json.decodeFromString<RestEasyTrainData>(response.bodyAsText())

        throw Exception("Error fetching train data: ${response.status}\n${response.bodyAsText()}")
    }
}