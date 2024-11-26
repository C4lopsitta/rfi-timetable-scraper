package cc.atomtech.timetable.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.models.TrenitaliaInfoLavori
import cc.atomtech.timetable.Strings

@Composable
fun TrenitaliaRegionInfo(selectedRegion: TrenitaliaInfoLavori?) {
    val uriHandler = LocalUriHandler.current

    Column {
        Text(
            selectedRegion?.regionName ?: Strings.get("undefined"),
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding( vertical = 12.dp )
        )
        LazyColumn {
            items(selectedRegion?.issues ?: listOf()) { issue ->
                Column(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current,
                        role = Role.Button,
                        onClickLabel = Strings.get("open_in_browser"),
                        onClick = {
                            if(issue.link != null) {
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
                HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
            }
        }
    }
}