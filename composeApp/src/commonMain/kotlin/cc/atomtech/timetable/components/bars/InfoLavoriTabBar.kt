package cc.atomtech.timetable.components.bars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Announcement
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import cc.atomtech.timetable.StringRes

@Composable
fun InfoLavoriTabBar(tabIndex: Int,
                     setIndex: (Int) -> Unit) {
    TabRow (
        selectedTabIndex = tabIndex
    ) {
        Tab(
            selected = true,
            onClick = { setIndex(0) },
            text = { Text(StringRes.get("trenitalia"), maxLines = 1, overflow = TextOverflow.Ellipsis) },
            icon = { Icon(Icons.Rounded.Train, contentDescription = StringRes.get("trenitalia")) }
        )
        Tab(
            selected = true,
            onClick = { setIndex(1) },
            text = { Text(StringRes.get("real_time"), maxLines = 1, overflow = TextOverflow.Ellipsis) },
            icon = { Icon(Icons.Rounded.Timelapse, contentDescription = StringRes.get("real_time")) }
        )
        Tab(
            selected = true,
            onClick = { setIndex(2) },
            text = { Text(StringRes.get("announcements"), maxLines = 1, overflow = TextOverflow.Ellipsis) },
            icon = { Icon(Icons.AutoMirrored.Rounded.Announcement, contentDescription = StringRes.get("announcements")) }
        )
    }
}
