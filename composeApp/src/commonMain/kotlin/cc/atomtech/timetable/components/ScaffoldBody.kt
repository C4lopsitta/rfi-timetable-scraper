package cc.atomtech.timetable.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ScaffoldBody(isLoading: Boolean,
                 isDesktop: Boolean,
                 paddingValues: PaddingValues,
                 navRail: @Composable() (() -> Unit)? = null,
                 content: @Composable () -> Unit) {

    if(isDesktop) {
        Surface (
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            if (isLoading)
                LinearProgressIndicator( modifier = Modifier.fillMaxWidth() )
            Row {
                if (navRail != null) navRail()
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
            if(isLoading)
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
            content()
        }
    }
}