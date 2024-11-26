package cc.atomtech.timetable.models

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.Strings

data class Station(val name: String, val id: Int) {
    @Composable
    fun toFavouritesRow(setAsStation: (Int) -> Unit) {
        Row (
            modifier = Modifier.fillMaxWidth()
                .padding( vertical = 12.dp )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    role = Role.Button,
                    onClickLabel = Strings.format("pick_station", name),
                    onClick = { setAsStation(id) }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            IconButton(
                onClick = {

                }
            ) {
                Icon(Icons.Rounded.Delete, contentDescription = Strings.get("delete"))
            }
        }
        HorizontalDivider()
    }

    override fun toString(): String {
        return "$id"
    }
}