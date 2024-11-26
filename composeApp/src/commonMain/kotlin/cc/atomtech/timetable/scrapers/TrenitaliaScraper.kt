package cc.atomtech.timetable.scrapers

import cc.atomtech.timetable.models.TrenitaliaInfo
import cc.atomtech.timetable.models.TrenitaliaInfoLavori
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Element
import java.util.Locale
import cc.atomtech.timetable.components.TrenitaliaIrregularTrafficDetails

private object ElementIds {
    const val REGULAR_TRAFFIC = "CIRCOLAZIONE_REGOLARE"
    const val IC_EC_INFO = "INFOTRENI_INTERCITY_-_EUROCITY"
    const val REGIONAL_INFO = "INFORMAZIONI_SUL_TRASPORTO_REGIONALE"
}

object TrenitaliaScraper {
    private const val baseUrl = "https://www.trenitalia.com/it/informazioni/Infomobilita/notizie-infomobilita.html"


    private fun String.stationName(): String {
        return this.lowercase(Locale.getDefault())
            .replace("''''", "'")
            .split(" ")
            .joinToString(" ") { word ->
                word.split(".")
                    .joinToString(".") { part ->
                        part.replaceFirstChar { it.uppercaseChar() }
                    }
            }
            .split(" ")
            .joinToString(" ") { word ->
                word.split("-")
                    .joinToString("-") { part ->
                        part.replaceFirstChar { it.uppercaseChar() }
                    }
            }
            .replace("-", " - ")
    }

    fun getInfoLavoriElement(element: Element): TrenitaliaInfoLavori {
        val regionName = element.getElementsByTag("a")[0].text().removePrefix("INFOLAVORI ").stationName()

        val paragraphs = element.getElementsByTag("p")

        val issues = arrayListOf<TrenitaliaIrregularTrafficDetails>()

        if(paragraphs.size > 1) {
            for (i in 0..<paragraphs.size step 2) {
                if (paragraphs[i].text().isBlank() || paragraphs[i].text().isEmpty()) break

                var link: String? = null
                try {
                    link = paragraphs[i].getElementsByTag("a")[0].attribute("href")?.value
                } catch (_: Exception) {}
                var title = ""
                try {
                    title = paragraphs[i].getElementsByTag(if (link != null) "a" else "b")[0].text()
                } catch (_: Exception) {}
                var body = ""
                try {
                    body = paragraphs[i + 1].text()
                } catch (_: Exception) {
                }

                // TODO)) Add a better serializer for the paragraph
                issues.add(
                    TrenitaliaIrregularTrafficDetails(
                        title = title,
                        link = link,
                        details = listOf(body)
                    )
                )
            }
        }

        return TrenitaliaInfoLavori(regionName, issues.toList())
    }

    suspend fun scrape(): TrenitaliaInfo {
        val page = Ksoup.parseGetRequest(baseUrl)

        val dataBody = page.getElementById("accordionGenericInfomob")
            ?: throw Exception("Unable to fetch data")
        val items = dataBody.getElementsByTag("li")

        val regularTraffic = items.find { it.getElementsByTag("a")[0].id() == ElementIds.REGULAR_TRAFFIC }
        val icEcInfo = items.find { it.getElementsByTag("a")[0].id() == ElementIds.IC_EC_INFO }
        val regionalInfo = items.find { it.getElementsByTag("a")[0].id() == ElementIds.REGIONAL_INFO }

        val irregularTrafficEvents = arrayListOf<TrenitaliaIrregularTrafficDetails>()
        for(item in items) {
            if(item.getElementsByTag("a").hasClass("inEvidenza")) {
                val irregularTrafficBody = arrayListOf<String>()

                for(p in item.getElementsByTag("p")) {
                    irregularTrafficBody.add(p.text())
                }

                irregularTrafficEvents.add(TrenitaliaIrregularTrafficDetails(
                    title = item.getElementsByTag("a")[0].text(),
                    details = irregularTrafficBody.toList()
                ))

//                items.remove(item)
            }
        }

        items.remove(regularTraffic)
        items.remove(icEcInfo)
        items.remove(regionalInfo)

        val infoLavori = arrayListOf<TrenitaliaInfoLavori>()

        for(item in items) {
            if(item.getElementsByTag("a")[0].id().contains("INFOLAVORI")) {
                infoLavori.add(getInfoLavoriElement(item))
//                items.remove(item)
            }
        }

        return TrenitaliaInfo(
            isTrafficRegular = regularTraffic != null,
            irregularTrafficEvents = irregularTrafficEvents,
            infoLavori = infoLavori.toList()
        )
    }
}