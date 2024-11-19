package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoLavori() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.Engineering,
            contentDescription = "Rail Works",
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(92.dp).height(92.dp)
        )
        Text(
            "Stay updated with rail accidents, issues, planned strikes and changes in service schedule. This feature is currently not yet implemented.",
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding( horizontal = 32.dp, vertical = 12.dp ),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}
