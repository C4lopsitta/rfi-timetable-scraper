package cc.atomtech.timetable.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TextField
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopSearch(searchSuggestions: List<Station>?,
                  navController: NavHostController,
                  setStationId: (Int) -> Unit) {
    var searchKey by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.fillMaxHeight()
            .padding( top = 24.dp )
            .fillMaxWidth(0.55f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchKey,
                    placeholder = { Text("Search for any station...") },
                    onQueryChange = { newQuery -> searchKey = newQuery },
                    onSearch = {

                    },
                    expanded = false,
                    onExpandedChange = {},
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Rounded.Search, contentDescription = "Search")
                        }
                    }
                )
            },
            expanded = false,
            onExpandedChange = { },
            content = {}
        )
        LazyColumn {
            items(searchSuggestions ?: listOf()) { suggestion ->
                Text(suggestion.name,
                    fontSize = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable(interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current,
                            role = Role.Button,
                            onClickLabel = "Click to open details",
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
