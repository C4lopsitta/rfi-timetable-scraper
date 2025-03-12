package cc.atomtech.timetable.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.TrenitaliaEventDetails

@Composable
fun TrenitaliaEventRow(
    event: TrenitaliaEventDetails,
    onClick: () -> Unit
) {
    var accurateTitle: String? = null
    var accurateDescription: String? = null

    if(event.title.contains(":")) {
        accurateTitle = event.title.split(":")[0]
        accurateDescription = event.title.split(":")[1].trim().replaceFirstChar {
            it.uppercase()
        }
    }

    ListItem(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = LocalIndication.current,
            role = Role.Button,
            onClickLabel = StringRes.get("view_issue_details"),
            onClick = { onClick() }
        ),
        headlineContent = {
            Text(
                if(accurateTitle == null) event.title else accurateTitle,
                fontWeight = if(event.title.contains("sciopero", ignoreCase = true)) FontWeight.SemiBold else FontWeight.Normal,
            )
        },
        supportingContent = {
            if(accurateDescription != null) {
                Text(accurateDescription)
            } else null
        },
        trailingContent = {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = StringRes.get("view_issue_details")
            )
        },
        leadingContent = {
            if(event.title.contains("sciopero", ignoreCase = true)) {
                Icon(
                    Icons.Rounded.Report,
                    contentDescription = ""
                )
            }
        }
    )
}