package cc.atomtech.timetable.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.RailwayAlert
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.models.TrenitaliaInfo
import cc.atomtech.timetable.scrapers.TrenitaliaScraper
import io.ktor.utils.io.CancellationException
import cc.atomtech.timetable.Strings
import cc.atomtech.timetable.components.TrenitaliaIrregularTrafficDetails
import cc.atomtech.timetable.components.TrenitaliaRegionCard
import cc.atomtech.timetable.models.TrenitaliaInfoLavori

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoLavoriTrenitalia(navigateToRegionDetails: (TrenitaliaInfoLavori) -> Unit) {
    var info by remember { mutableStateOf<TrenitaliaInfo?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    var chosenEvent by remember { mutableStateOf<TrenitaliaIrregularTrafficDetails?>(null) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        try {
            info = TrenitaliaScraper.scrape()
        } catch (_: CancellationException) {
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding( top = 12.dp, bottom = 12.dp, end = 12.dp )
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
                        contentDescription = Strings.get("traffic_regular"),
                        modifier = Modifier.height(40.dp).width(40.dp)
                    )
                } else {
                    Icon(
                        Icons.Rounded.RailwayAlert,
                        contentDescription = Strings.get("traffic_irregular"),
                        modifier = Modifier.height(40.dp).width(40.dp)
                    )
                }
                Text(
                    Strings.get(if (info!!.isTrafficRegular) "traffic_regular" else "traffic_irregular"),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding( start = 12.dp )
                )
            }
            if(!info!!.isTrafficRegular) {
                for (event in info!!.irregularTrafficEvents) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current,
                            role = Role.Button,
                            onClickLabel = Strings.get("view_issue_details"),
                            onClick = {
                                chosenEvent = event
                                showSheet = true
                            }
                        )
                            .fillMaxWidth()
                    ) {
                        Text(
                            event.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .fillMaxWidth(0.9f)
                        )
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = Strings.get("view_issue_details")
                        )
                    }
                    HorizontalDivider( modifier = Modifier.padding( bottom = 12.dp ) )
                }
            }
            Text(
                Strings.get("regions"),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding( vertical = 12.dp )
            )
            LazyRow (
            ) {
                items(info!!.infoLavori) { regionInfo ->
                    TrenitaliaRegionCard(
                        name = regionInfo.regionName,
                        noticesAvailable = 0,
                    ) { navigateToRegionDetails(regionInfo) }
                }
            }
            if(showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState
                ) {
                    Column (
                        modifier = Modifier.padding( 12.dp )
                    ) {
                        Text(chosenEvent?.title ?: "", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                        HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
                        LazyColumn {
                            items(chosenEvent?.details ?: listOf()) { detail ->
                                if(detail.contains("Aggiornamento - ore") ||
                                    detail.contains("Inizio evento - ore")) {
                                    Text(
                                        detail,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding( vertical = 12.dp )
                                    )
                                } else {
                                    Text(detail, modifier = Modifier.padding( bottom = 12.dp ))
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
