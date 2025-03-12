package cc.atomtech.timetable.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkRemove
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.viewmodels.Station

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationSearch(
    stationData: Station,
    navController: NavHostController
) {
    var query by remember { mutableStateOf("") }
    val resutls by derivedStateOf {
        stationData.allStationData.value.filter { it.name.contains(query, ignoreCase = true) }
    }

    LazyColumn (

    ) {
//        item {
//            Text(
//                StringRes.get("search_title"),
//                fontSize = 28.sp,
//                fontWeight = FontWeight.SemiBold,
//                modifier = Modifier.padding( bottom = 12.dp )
//            )
//        }
        item {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = {},
                        expanded = false,
                        onExpandedChange = {},
                        placeholder = { Text(StringRes.get("search_title")) },
                        trailingIcon = {
                            Icon(Icons.Rounded.Search, contentDescription = "TODO")
                        },
                    )
                },
                expanded = false,
                onExpandedChange = {},
                content = {},
                modifier = Modifier.padding( bottom = 12.dp, end = 12.dp ).fillMaxWidth()
            )
        }
        items(resutls) { result ->
            ListItem(
                headlineContent = { Text(result.name) },
                supportingContent = { Text(result.stationCountry.toString()) },
                trailingContent = {
                    var isBookmarked by remember { mutableStateOf( result.isBookmarked ) }
                    IconButton(
                        content = {
                            Icon(
                                if(!isBookmarked) Icons.Outlined.BookmarkAdd else Icons.Rounded.BookmarkRemove,
                                contentDescription = ""
                            )
                        },
                        onClick = {
                            isBookmarked = !isBookmarked
                            result.isBookmarked = isBookmarked
                        }
                    )
                },
                modifier = Modifier.clickable(
                    onClick = {
                        stationData.updateStation(result)
                        navController.popBackStack()
                    }
                )
            )
        }
    }
}