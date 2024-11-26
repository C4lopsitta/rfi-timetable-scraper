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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cc.atomtech.timetable.models.TrenitaliaInfoLavori
import cc.atomtech.timetable.Strings
import cc.atomtech.timetable.components.RowLink

@Composable
fun TrenitaliaRegionInfo(
    selectedRegion: TrenitaliaInfoLavori?,
    navController: NavHostController
) {
    val uriHandler = LocalUriHandler.current

    Column {
        Row {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.width(48.dp).height(48.dp).align(Alignment.CenterVertically)
            ) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = Strings.get("back"))
            }
            Text(
                selectedRegion?.regionName ?: Strings.get("undefined"),
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 12.dp, end = 12.dp, bottom = 12.dp)
            )
        }
        HorizontalDivider()
        Column {
            RowLink(
                link = selectedRegion?.worksAndServiceModificationsLink,
                text = Strings.get("works_and_service_modifications")
            )
            RowLink(
                link = selectedRegion?.busServiceLink,
                text = Strings.get("bus_service_stops")
            )
        }
        HorizontalDivider( modifier = Modifier.padding( bottom = 12.dp ) )
        if(selectedRegion?.issues?.size == 0) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    Icons.Rounded.Celebration,
                    contentDescription = Strings.get("no_announcements"),
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(92.dp).height(92.dp)
                )
                Text(
                    Strings.format("no_announcements_desc", selectedRegion.regionName),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding( horizontal = 32.dp, vertical = 12.dp ),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn {
                items(selectedRegion?.issues ?: listOf()) { issue ->
                    Column(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current,
                            role = Role.Button,
                            onClickLabel = Strings.get("open_in_browser"),
                            onClick = {
                                if (issue.link != null) {
                                    uriHandler.openUri(issue.link)
                                }
                            }
                        )
                    ) {
                        Text(
                            issue.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(issue.details[0])
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                }
            }
        }
    }
}