package cc.atomtech.timetable.views

//import androidx.compose.foundation.LocalIndication
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.Star
//import androidx.compose.material.icons.rounded.Search
//import androidx.compose.material.icons.rounded.Star
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.semantics.Role
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import cc.atomtech.timetable.StringRes
//import cc.atomtech.timetable.models.viewmodels.Station
//
//@Composable
//fun MobileSearch(stationData: Station,
//                 searchSuggestions: List<Station>?,
//                 navController: NavHostController,
//                 favouriteStations: Stations,
//                 updateFavourites: (String) -> Unit,
//                 setStationId: (Int) -> Unit) {
//    favouriteStations.stations.forEach { println("${it.name} is favourite") }
//
//
//    if (stations.stations.isNotEmpty() && searchSuggestions?.isNotEmpty() == true) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 12.dp)
//        ) {
//            items(searchSuggestions) { suggestion ->
//                val isFavourite by remember { derivedStateOf { favouriteStations.searchById(suggestion.id) != null } }
//
//                Row (
//                    modifier = Modifier.fillMaxWidth()
//                        .padding(vertical = 12.dp)
//                        .clickable(interactionSource = remember { MutableInteractionSource() },
//                            indication = LocalIndication.current,
//                            role = Role.Button,
//                            onClickLabel = StringRes.format("pick_station", suggestion.name),
//                            onClick = {
//                                setStationId(suggestion.id)
//                                navController.popBackStack()
//                            }
//                        ),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(suggestion.name, fontSize = 24.sp)
//                    IconButton(
//                        onClick = {
//                            if(favouriteStations.searchById(suggestion.id) == null) {
//                                favouriteStations.stations.add(suggestion)
//                            } else {
//                                favouriteStations.stations.remove(
//                                    favouriteStations.searchById(suggestion.id)
//                                )
//                            }
//                            updateFavourites(favouriteStations.toString())
//                            favouriteStations.stations = favouriteStations.stations
//                        },
//                        content = {
//                            Icon(
//                                if(isFavourite) Icons.Rounded.Star else Icons.Outlined.Star,
//                                contentDescription = StringRes.get("favourite_station")
//                            )
//                        }
//                    )
//                }
//                HorizontalDivider()
//            }
//        }
//    } else {
//        Column (
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(
//                Icons.Rounded.Search,
//                contentDescription = StringRes.get("search"),
//                tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier.width(92.dp).height(92.dp)
//            )
//            Text(
//                StringRes.get("station_search_details"),
//                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier.padding( horizontal = 32.dp, vertical = 12.dp ),
//                fontSize = 18.sp,
//                textAlign = TextAlign.Center
//            )
//        }    }
//}
//
