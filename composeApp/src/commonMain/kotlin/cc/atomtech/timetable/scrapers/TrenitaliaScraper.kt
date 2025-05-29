package cc.atomtech.timetable.scrapers

import cc.atomtech.timetable.models.TrenitaliaInfo
import cc.atomtech.timetable.models.TrenitaliaInfoLavori
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Element
import cc.atomtech.timetable.models.TrenitaliaEventDetails

private object ElementIds {
    const val REGULAR_TRAFFIC = "CIRCOLAZIONE_REGOLARE"
    const val IC_EC_INFO = "INFOTRENI_INTERCITY_-_EUROCITY"
    const val REGIONAL_INFO = "INFORMAZIONI_SUL_TRASPORTO_REGIONALE"
    const val FR_INFO = "INFOTRENI_FRECCE_"
}

@Deprecated("Replaced by newer object.", ReplaceWith("cc.atomtech.timetable.apis.TrenitaliaRestEasy"), DeprecationLevel.ERROR)
object TrenitaliaScraper {
    private const val baseUrl = "https://www.trenitalia.com/it/informazioni/Infomobilita/notizie-infomobilita.html"
    private const val cercaTrenoFinderUrl = "http://www.viaggiatreno.it/infomobilitamobile/resteasy/viaggiatreno/cercaNumeroTrenoTrenoAutocomplete/"

    private fun getAndamentoTrenoUrl(trainData: TrenitaliaTrainData): String {
        return "http://www.viaggiatreno.it/infomobilita/resteasy/viaggiatreno/andamentoTreno/${trainData.originStationId}/${trainData.number}/${trainData.departureTime}"
    }

    private fun String.stationName(): String {
        return this.lowercase()
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

    @Deprecated("Deprecated since v.1.5.0 due to HTML changes by Trenitalia", level = DeprecationLevel.WARNING)
    private fun getInfoLavoriElement(element: Element): TrenitaliaInfoLavori {
        val regionName = element.getElementsByTag("a")[0].text().removePrefix("INFOLAVORI ").stationName()

        val paragraphs = element.getElementsByTag("p")

        val issues = arrayListOf<TrenitaliaEventDetails>()

        var busLink = ""
        var worksAndModificationsToServiceLink = ""

        val busElement = paragraphs.find {
            it.text().contains("Punti di fermata bus Trenitalia")
        }
        if(busElement != null) {
            busLink = busElement.getElementsByTag("a")[0].attribute("href")?.value ?: ""
            paragraphs.remove(busElement)
        }

        val worksAndModificationsToServiceElement = paragraphs.find {
            it.text().contains("Lavori e modifiche al servizio")
        }
        if(worksAndModificationsToServiceElement != null) {
            worksAndModificationsToServiceLink = worksAndModificationsToServiceElement.getElementsByTag("a")[0].attribute("href")?.value ?: ""
            paragraphs.remove(worksAndModificationsToServiceElement)
        }

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
                    TrenitaliaEventDetails(
                        title = title,
                        link = link,
                        details = listOf(body)
                    )
                )
            }
        }

        return TrenitaliaInfoLavori(
            regionName = regionName,
            issues = issues.toList(),
            busServiceLink = busLink,
            worksAndServiceModificationsLink = worksAndModificationsToServiceLink
        )
    }

    @Deprecated("Deprecated since v.1.5.0 due to HTML changes by Trenitalia", level = DeprecationLevel.WARNING)
    private fun getExtraEvent(element: Element): TrenitaliaEventDetails {
        val date: String = element.getElementsByTag("h4")[0].text()
        val title: String = element.getElementsByTag("a")[0].text()
        val details: ArrayList<String> = arrayListOf()

        for(p in element.getElementsByClass("info-text")[0].children()) {
            details.add(p.text())
        }

        return TrenitaliaEventDetails(
            title = title,
            details = details.toList(),
            date = date
        )
    }

    @Deprecated("Deprecated since v.1.5.0 due to HTML changes by Trenitalia", level = DeprecationLevel.WARNING)
    suspend fun scrapePassengerInformation(): TrenitaliaInfo {
        val page = Ksoup.parseGetRequest(baseUrl)

        val dataBody = page.getElementById("accordionGenericInfomob")
            ?: throw Exception("Unable to fetch data")
        val items = dataBody.getElementsByTag("li")

        // TODO)) Finish this
        val regularTraffic = items.find { it.getElementsByTag("a")[0].id() == ElementIds.REGULAR_TRAFFIC }
        val frInfo = items.find { it.getElementsByTag("a")[0].id().contains(ElementIds.FR_INFO) }
        val icEcInfo = items.find { it.getElementsByTag("a")[0].id() == ElementIds.IC_EC_INFO }
        val regionalInfo = items.find { it.getElementsByTag("a")[0].id() == ElementIds.REGIONAL_INFO }

        val irregularTrafficEvents = arrayListOf<TrenitaliaEventDetails>()
        for(item in items) {
            if(item.getElementsByTag("a").hasClass("inEvidenza")) {
                val irregularTrafficBody = arrayListOf<String>()

                for(p in item.getElementsByTag("p")) {
                    irregularTrafficBody.add(p.text())
                }

                irregularTrafficEvents.add(
                    TrenitaliaEventDetails(
                    title = item.getElementsByTag("a")[0].text(),
                    details = irregularTrafficBody.toList()
                )
                )
            }
        }

        items.remove(regularTraffic)
        items.remove(icEcInfo)
        items.remove(frInfo)
        items.remove(regionalInfo)

        val infoLavori = arrayListOf<TrenitaliaInfoLavori>()

        for(item in items) {
            if(item.getElementsByTag("a")[0].id().contains("INFOLAVORI")) {
                infoLavori.add(getInfoLavoriElement(item))
            }
        }
        for(item in items.filter{ it.getElementsByTag("a")[0].id().contains("INFOLAVORI") }) {
            items.remove(item)
        }
        items.remove(
            items.find { it.getElementsByTag("a")[0].id().contains("INFOLEGENDA") }
        )

        val extraEvents: ArrayList<TrenitaliaEventDetails> = arrayListOf()

        items.forEach { it ->
            extraEvents.add(getExtraEvent(it))
        }

        return TrenitaliaInfo(
            isTrafficRegular = regularTraffic != null,
            irregularTrafficEvents = irregularTrafficEvents,
            extraEvents = extraEvents.toList(),
            infoLavori = infoLavori.toList()
        )
    }
}