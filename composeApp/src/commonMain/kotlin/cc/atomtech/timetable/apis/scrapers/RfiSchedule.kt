package cc.atomtech.timetable.apis.scrapers

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

/**
 * @since 1.5.0
 */
object RfiSchedule {
    final val searchUrl = "https://prm.rfi.it/qo_prm/WebService.asmx/GetCompletionList"

    suspend fun searchString(station: String) {
        val response = HttpClient(CIO).post(
            urlString = searchUrl,
            block = {
                headers["User-Agent"] = "Mozilla/5.0 (Windows NT 10.0; rv:128.0) Gecko/20100101 Firefox/128.0"
                headers["Referer"] = "https://prm.rfi.it/qo_prm/"
                headers["Origin"] = "https://prm.rfi.it"
                headers["Host"] = "prm.rfi.it"
                headers["Content-Type"] = "application/json; charset=utf-8"
            }
        )

        if(response.status.value >= 300 || response.status.value < 200) {
            throw Exception("Failed to get search results")
        }

        val jsonResponse = Json.decodeFromString<Map<String, String>>(response.bodyAsText())
    }
}