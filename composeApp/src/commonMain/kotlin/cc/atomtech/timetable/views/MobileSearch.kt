package cc.atomtech.timetable.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cc.atomtech.timetable.models.Station
import cc.atomtech.timetable.models.Stations

@Composable
fun MobileSearch(stations: Stations,
                 searchSuggestions: List<Station>?,
                 navController: NavHostController,
                 favouriteStations: MutableSet<String>,
                 setStationId: (Int) -> Unit) {
    if (stations.stations.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            items(searchSuggestions ?: listOf()) { suggestion ->
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable(interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current,
                            role = Role.Button,
                            onClickLabel = "Pick ${suggestion.name} as station",
                            onClick = {
                                setStationId(suggestion.id)
                                navController.popBackStack()
                            }
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(suggestion.name, fontSize = 24.sp)
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            if(!favouriteStations.contains(suggestion.id.toString())) Icons.Outlined.Star else Icons.Rounded.Star,
                            contentDescription = "Favourite location"
                        )
                    }
                }
                HorizontalDivider()
            }
        }
    }}
