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
import androidx.compose.material3.Icon
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
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = LocalIndication.current,
            role = Role.Button,
            onClickLabel = StringRes.get("view_issue_details"),
            onClick = { onClick() }
        )
            .fillMaxWidth()
    ) {
        Text(
            event.title,
            fontSize = 20.sp,
            fontWeight = if(event.title.contains("sciopero", ignoreCase = true)) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(0.9f)
        )
        Column {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = StringRes.get("view_issue_details")
            )
            if(event.title.contains("sciopero", ignoreCase = true)) {
                
            }
        }
    }
}