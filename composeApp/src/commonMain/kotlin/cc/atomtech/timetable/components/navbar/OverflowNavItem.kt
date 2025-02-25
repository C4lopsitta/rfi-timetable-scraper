package cc.atomtech.timetable.components.navbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OverflowNavItem(
    isDisabled: Boolean = false,
    text: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {

    Row (
        modifier = Modifier.clickable( onClick = { if(!isDisabled) onClick() } )
            .padding( vertical = 24.dp )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.padding( horizontal = 12.dp )
        ) {
            icon()
        }
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Normal)
    }
    HorizontalDivider()
}