package cc.atomtech.timetable.views.notices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.scrapers.RssFeeds
import cc.atomtech.timetable.models.FeedItem
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.TrenitaliaInfoLavori
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun InfoLavori(tabIndex: Int,
               navigateToRegionDetails: (TrenitaliaInfoLavori) -> Unit) {
    var baseFeed by remember { mutableStateOf<List<FeedItem>>(listOf()) }
    var baseFeedAnnouncements by remember { mutableStateOf<List<FeedItem>>(listOf()) }

    LaunchedEffect(Unit) {
        try {
            baseFeed = RssFeeds.parseFeed(RssFeeds.fetchRss(RssFeeds.allRegionsLive))
            baseFeedAnnouncements = RssFeeds.parseFeed(RssFeeds.fetchRss(RssFeeds.allRegionsNotices), isAnnouncements = true)
        } catch (_: CancellationException) {
        } catch(e: Exception) {
            println(e.printStackTrace())
        }
    }

    Column (
        modifier = Modifier.fillMaxSize().padding( end = 12.dp ),
    ) {
        when(tabIndex) {
            1 -> LazyColumn {
                items(baseFeed) { item ->
                    item.toMobileRow()
                    HorizontalDivider()
                }
            }
            2 -> LazyColumn {
                items(baseFeedAnnouncements) { item ->
                    item.toMobileRow()
                    HorizontalDivider()
                }
            }
            0 -> InfoLavoriTrenitalia(navigateToRegionDetails = { navigateToRegionDetails(it) })
            else -> Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    Icons.Rounded.Engineering,
                    contentDescription = StringRes.get("rail_works"),
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(92.dp).height(92.dp)
                )
                Text(
                    StringRes.get("rail_works_details"),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding( horizontal = 32.dp, vertical = 12.dp ),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}
