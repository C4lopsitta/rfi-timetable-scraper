package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Announcement
import androidx.compose.material.icons.rounded.Announcement
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import cc.atomtech.timetable.RssFeeds
import cc.atomtech.timetable.models.FeedItem
import cc.atomtech.timetable.Strings
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun InfoLavori() {
    var tabIndex by remember { mutableStateOf(0) }
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

        TabRow (
            selectedTabIndex = tabIndex
        ) {
            Tab(
                selected = true,
                onClick = { tabIndex = 0 },
                text = { Text(Strings.get("real_time")) },
                icon = { Icon(Icons.Rounded.Timelapse, contentDescription = Strings.get("real_time")) }
            )
            Tab(
                selected = true,
                onClick = { tabIndex = 1 },
                text = { Text(Strings.get("announcements")) },
                icon = { Icon(Icons.AutoMirrored.Rounded.Announcement, contentDescription = Strings.get("announcements")) }
            )
            Tab(
                selected = true,
                onClick = { tabIndex = 2 },
                text = { Text(Strings.get("trenitalia")) },
                icon = { Icon(Icons.Rounded.Train, contentDescription = Strings.get("trenitalia")) }
            )
        }

        when(tabIndex) {
            0 -> LazyColumn {
                items(baseFeed) { item ->
                    item.toMobileRow()
                    HorizontalDivider()
                }
            }
            1 -> LazyColumn {
                items(baseFeedAnnouncements) { item ->
                    item.toMobileRow()
                    HorizontalDivider()
                }
            }
            else -> Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    Icons.Rounded.Engineering,
                    contentDescription = Strings.get("rail_works"),
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(92.dp).height(92.dp)
                )
                Text(
                    Strings.get("rail_works_details"),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding( horizontal = 32.dp, vertical = 12.dp ),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}
