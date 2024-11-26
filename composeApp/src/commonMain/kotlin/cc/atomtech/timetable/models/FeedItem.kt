package cc.atomtech.timetable.models

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class FeedItem (val title: String, val url: String, val pubDate: String? = null) {
    @Composable
    fun toMobileRow() {
        val uriHandler = LocalUriHandler.current

        Column (
            modifier = Modifier.fillMaxWidth()
                .padding( vertical = 12.dp )
                .clickable(interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    role  = Role.Button,
                    onClickLabel = "Click to open details",
                    onClick = {
                        uriHandler.openUri(url)
                    }),
        ) {
            Row (
                modifier = Modifier.fillMaxWidth().height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title.split(":")[0], fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Icon(Icons.AutoMirrored.Rounded.OpenInNew, contentDescription = "Open in Browser")
            }
            Text(title.split(": ")[1])
            Text(pubDate ?: "", fontSize = 12.sp, fontWeight = FontWeight.Light)
        }
    }


    override fun toString(): String {
        return "FeedItem $title -> $url @ $pubDate"
    }
}
