package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FavouriteStations(favouriteStations: MutableSet<String>) {
    if(favouriteStations.isNotEmpty()) {
        LazyColumn {
            items(favouriteStations.toList()) {
                Text(it)
            }
        }
    } else {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = "Star",
                tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(92.dp).height(92.dp)
            )
            Text(
                "Search and add your favourite stations for quick access",
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding( horizontal = 32.dp, vertical = 12.dp ),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
