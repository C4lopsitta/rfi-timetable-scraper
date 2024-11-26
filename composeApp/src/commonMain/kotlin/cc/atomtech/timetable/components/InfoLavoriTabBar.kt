package cc.atomtech.timetable.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Announcement
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cc.atomtech.timetable.Strings

@Composable
fun InfoLavoriTabBar(tabIndex: Int,
                     setIndex: (Int) -> Unit) {
    TabRow (
        selectedTabIndex = tabIndex
    ) {
        Tab(
            selected = true,
            onClick = { setIndex(0) },
            text = { Text(Strings.get("real_time")) },
            icon = { Icon(Icons.Rounded.Timelapse, contentDescription = Strings.get("real_time")) }
        )
        Tab(
            selected = true,
            onClick = { setIndex(1) },
            text = { Text(Strings.get("announcements")) },
            icon = { Icon(Icons.AutoMirrored.Rounded.Announcement, contentDescription = Strings.get("announcements")) }
        )
        Tab(
            selected = true,
            onClick = { setIndex(2) },
            text = { Text(Strings.get("trenitalia")) },
            icon = { Icon(Icons.Rounded.Train, contentDescription = Strings.get("trenitalia")) }
        )
    }
}