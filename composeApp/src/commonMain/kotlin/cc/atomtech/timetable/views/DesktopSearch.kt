package cc.atomtech.timetable.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cc.atomtech.timetable.models.Station
import cc.atomtech.timetable.models.Stations
import cc.atomtech.timetable.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopSearch(stations: Stations?,
                  navController: NavHostController,
                  setStationId: (Int) -> Unit) {
    var searchKey by remember { mutableStateOf("") }
    var searchSuggestions by remember { mutableStateOf<List<Station>>(stations?.stations ?: listOf()) }

    Column (
        modifier = Modifier.fillMaxSize()
            .padding( top = 12.dp ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(0.55f),
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchKey,
                    placeholder = { Text(Strings.get("station_search_placeholder")) },
                    onQueryChange = {
                        newQuery -> searchKey = newQuery
                        if(stations?.stations?.isNotEmpty() == true) {
                            searchSuggestions = stations.search(searchKey)
                        }
                    },
                    onSearch = {  },
                    expanded = false,
                    onExpandedChange = {},
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Rounded.Search, contentDescription = Strings.get("search"))
                        }
                    }
                )
            },
            expanded = false,
            onExpandedChange = { },
            content = {}
        )
        LazyColumn (
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            item {
                Text(Strings.get("search_results"), modifier = Modifier.padding( top = 16.dp ))
            }
            items(searchSuggestions ?: listOf()) { suggestion ->
                Text(suggestion.name,
                    fontSize = 24.sp,
                    lineHeight = 56.sp,
                    modifier = Modifier.fillMaxWidth()
                        .height(72.dp)
                        .padding( horizontal = 12.dp )
                        .clickable(interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current,
                            role = Role.Button,
                            onClickLabel = Strings.format("pick_station", suggestion.name),
                            onClick = {
                                setStationId(suggestion.id)
                                navController.popBackStack()
                            }
                        ))
                HorizontalDivider()
            }
        }
    }
}
