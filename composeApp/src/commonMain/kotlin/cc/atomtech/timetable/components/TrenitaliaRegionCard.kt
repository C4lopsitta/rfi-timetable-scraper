package cc.atomtech.timetable.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Card
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

@Composable
fun TrenitaliaRegionCard(
    name: String,
    noticesAvailable: Int = 0,
    onClick: () -> Unit
) {
    Card (
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .height(128.dp)
            .padding( end = 12.dp )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                role  = Role.Button,
                onClickLabel = Strings.format("click_go_to_region", name),
                onClick = { onClick() }
            )
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding( vertical = 8.dp, horizontal = 12.dp ),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
//            IconButton(
//                onClick = {  }
//            ) { Icon(Icons.Rounded.Favorite, contentDescription = "") }
            Text(Strings.format("notices_available", "$noticesAvailable"))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(name, fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = Strings.format("click_go_to_region", name),
                    modifier = Modifier.padding( start =  12.dp )
                )
            }
        }
    }
}
