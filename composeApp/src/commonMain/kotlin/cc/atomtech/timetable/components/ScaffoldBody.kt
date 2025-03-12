package cc.atomtech.timetable.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cc.atomtech.timetable.models.viewmodels.Station


@Composable
fun ScaffoldBody(isDesktop: Boolean,
                 stationData: Station,
                 paddingValues: PaddingValues,
                 navRail: @Composable() (() -> Unit)? = null,
                 content: @Composable () -> Unit) {
    val isLoading by derivedStateOf {
        stationData.loadingDepartures.value || stationData.loadingArrivals.value || stationData.loadingStations.value
    }

    if(isDesktop) {
        Surface (
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            Row {
                if (navRail != null) navRail()
                if( isLoading ) androidx.compose.material3.LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                content()
            }
        }
    } else {
        Surface(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ).fillMaxWidth()
        ) {
            if( isLoading ) androidx.compose.material3.LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            content()
        }
    }
}