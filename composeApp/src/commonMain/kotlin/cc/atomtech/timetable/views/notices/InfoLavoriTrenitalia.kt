package cc.atomtech.timetable.views.notices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RailwayAlert
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.models.TrenitaliaInfo
//import cc.atomtech.timetable.scrapers.TrenitaliaScraper
import io.ktor.utils.io.CancellationException
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.TrenitaliaEventDetails
import cc.atomtech.timetable.components.TrenitaliaEventRow
import cc.atomtech.timetable.components.TrenitaliaRegionCard
import cc.atomtech.timetable.models.TrenitaliaInfoLavori

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoLavoriTrenitalia(navigateToRegionDetails: (TrenitaliaInfoLavori) -> Unit) {
    var info by remember { mutableStateOf<TrenitaliaInfo?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    var chosenEvent by remember { mutableStateOf<TrenitaliaEventDetails?>(null) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        try {
//            info = TrenitaliaScraper.scrapePassengerInformation()
        } catch (_: CancellationException) {
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding( top = 12.dp, bottom = 12.dp )
    ) {
        if (info != null) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(60.dp)
                    .padding( bottom = 12.dp )
            ) {
                if(info!!.isTrafficRegular) {
                    Icon(
                        Icons.Rounded.Train,
                        contentDescription = StringRes.get("traffic_regular"),
                        modifier = Modifier.height(40.dp).width(40.dp)
                    )
                } else {
                    Icon(
                        Icons.Rounded.RailwayAlert,
                        contentDescription = StringRes.get("traffic_irregular"),
                        modifier = Modifier.height(40.dp).width(40.dp)
                    )
                }
                Text(
                    StringRes.get(if (info!!.isTrafficRegular) "traffic_regular" else "traffic_irregular"),
                    fontSize = 28.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding( start = 12.dp )
                )
            }
            LazyColumn {
                // headline events
                item {
                    if (!info!!.isTrafficRegular) {
                        for (event in info!!.irregularTrafficEvents) {
                            TrenitaliaEventRow(event) {
                                chosenEvent = event
                                showSheet = true
                            }
                        }
                    }
                }

                // region list
                item {
                    Text(
                        StringRes.get("regions"),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    LazyRow {
                        items(info!!.infoLavori) { regionInfo ->
                            TrenitaliaRegionCard(
                                name = regionInfo.regionName,
                                noticesAvailable = regionInfo.issues.size,
                            ) { navigateToRegionDetails(regionInfo) }
                        }
                    }
                }

                // past/future events
                item {
                    Text(
                        StringRes.get("past_future_events"),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding( bottom = 12.dp, top = 48.dp )
                    )
                }
                val liveNoticesTitles = List<String>(info!!.irregularTrafficEvents.size) { index ->
                    info!!.irregularTrafficEvents[index].title
                }

                items (info!!.extraEvents) {
                    if(!liveNoticesTitles.contains(it.title)) {
                        TrenitaliaEventRow(it) {
                            chosenEvent = it
                            showSheet = true
                        }
                    }
                }
            }
            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            chosenEvent?.title ?: "",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if(chosenEvent?.date != null) {
                            Text(
                                chosenEvent?.date ?: "",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding( vertical = 8.dp )
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        LazyColumn {
                            items(chosenEvent?.details ?: listOf()) { detail ->
                                if (detail.contains("Aggiornamento - ore") ||
                                    detail.contains("Inizio evento - ore") ||
                                    detail.contains("Fine evento - ore")
                                ) {
                                    Text(
                                        detail,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(vertical = 12.dp)
                                    )
                                } else {
                                    if(detail.contains("•")) {
                                        detail.split("•").forEach {
                                            if(it.isNotBlank()) Text("\u2022 $it")
                                        }
                                        Box( modifier = Modifier.padding( vertical = 6.dp ) )
                                    } else {
                                        Text(detail, modifier = Modifier.padding(bottom = 12.dp))
                                    }
                                }
                            }
                            if(chosenEvent?.link != null) {
                                item {
                                    val uriHandler = LocalUriHandler.current
                                    OutlinedButton(
                                        content = {
                                            Text(StringRes.get("open_in_browser"))
                                        },
                                        onClick = {
                                            chosenEvent!!.link?.let { uriHandler.openUri(it) }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            CircularProgressIndicator()
        }
    }
}
