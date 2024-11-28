package cc.atomtech.timetable.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.components.LargeIconText

@Composable
fun DeviceOffline(reload: () -> Unit) {
    LargeIconText(
        icon = Icons.Rounded.CloudOff,
        text = StringRes.get("offline"),
        actions = {
            IconButton(
                content = {Icon(
                    Icons.Rounded.Refresh,
                    contentDescription = "Reload",
                    tint = MaterialTheme.colorScheme.onSurface
                ) },
                onClick = { reload() }
            )
        }
    )
}