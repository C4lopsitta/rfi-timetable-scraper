package cc.atomtech.timetable.scrapers

import cc.atomtech.timetable.models.FeedItem
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.parser.Parser

@Deprecated("Will be moved new RfiNoticesFeeds object", ReplaceWith("cc.atomtech.timetable.apis.RfiNoticesFeeds"), DeprecationLevel.WARNING)
object RssFeeds {
    const val allRegionsLive = "https://www.rfi.it/it/news-e-media/infomobilita.rss.updates."
    const val allRegionsNotices = "https://www.rfi.it/it/news-e-media/infomobilita.rss.notices."

    val regions = listOf("abruzzo",
        "basilicata",
        "calabria",
        "campania",
        "emilia_romagna",
        "friuli_venezia_giulia",
        "lazio",
        "liguira",
        "lombardia",
        "marche",
        "molise",
        "piemonte",
        "puglia",
        "sardegna",
        "sicilia",
        "toscana",
        "trentino_alto_adige",
        "umbria",
        "valle_d_aosta",
        "veneto")

    suspend fun fetchRss(feed: String, region: String? = null): Document {
        val url = "$feed${ if(region == null) "xml" else "$region.xml" }"

        return Ksoup.parseGetRequest(url = url, parser = Parser.xmlParser())
    }

    fun parseFeed(feed: Document, isAnnouncements: Boolean = false): List<FeedItem> {
        val root = feed.getElementsByTag("channel")[0]

        val items = arrayListOf<FeedItem>()

        for (item in root.getElementsByTag("item")) {
            if(isAnnouncements) {
                items.add(
                    FeedItem(
                        title = item.getElementsByTag("title")[0].text(),
                        url = item.getElementsByTag("link")[0].text(),
                        pubDate = item.getElementsByTag("pubDate")[0].text()
                    )
                )
            } else {
                val parts = item.getElementsByTag("title")[0].text().split(":", ",")

                items.add(
                    FeedItem(
                        title = parts[0],
                        description = item.getElementsByTag("title")[0].text()
                            .removePrefix(parts[0])
                            .removeRange(0..0)
                            .trimStart()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        url = item.getElementsByTag("link")[0].text(),
                        pubDate = item.getElementsByTag("pubDate")[0].text()
                    )
                )
            }
        }

        return items.toList()
    }
}