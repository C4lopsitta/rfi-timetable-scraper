package cc.atomtech.timetable.views

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.StringRes
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingSliderItem(
    title: String,
    subTitle: String? = null,
    steps: Int,
    range: ClosedFloatingPointRange<Float>,
    initialValue: Float,
    stringValues: List<String>? = null,
    onValueChange: (Float) -> Unit
) {
    var localValue by remember { mutableStateOf(initialValue) }
    Column (
        modifier = Modifier.fillMaxWidth().padding( vertical = 8.dp )
    ) {
        Text(title, fontSize = 20.sp)
        if(subTitle != null) Text(subTitle)
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                if(stringValues != null) {
                    stringValues[localValue.roundToInt()]
                } else { "${localValue.roundToInt()}" },
                modifier = Modifier.padding( horizontal = 8.dp )
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = localValue,
                onValueChange = {
                    localValue = it
                    onValueChange(it)
                },
                steps = steps,
                valueRange = range
            )
        }
    }
}

@Composable
private fun SettingToggleItem(
    title: String,
    subTitle: String? = null,
    isBetaFeature: Boolean = false,
    initialValue: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    var localValue by remember { mutableStateOf(initialValue) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding( vertical = 8.dp )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                role  = Role.Switch,
                onClick =  {
                    localValue = !localValue
                    onValueChange(localValue)
                }
            )
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.Center
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(isBetaFeature) Card (
                    modifier = Modifier.padding( vertical = 4.dp ),
                    colors = CardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
                        disabledContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text("BETA", modifier = Modifier.padding( horizontal = 8.dp ))
                }
                Text(title, fontSize = 20.sp, modifier = Modifier.padding( start = if(isBetaFeature) 8.dp else 0.dp ))
            }
            if (subTitle != null) Text(subTitle)
        }
        Switch(
            checked = localValue,
            modifier = Modifier.width(64.dp),
            onCheckedChange = {
                localValue = !localValue
                onValueChange(localValue)
            }
        )
    }

}


@Composable
fun Settings() {
    LazyColumn (
        modifier = Modifier.fillMaxSize().padding( end = 12.dp )
    ) {
        item {
            Text(
                "Bear in mind, these settings are dummies right now!\nRicorda che queste impostazioni non funzionano per ora!",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        item {
            SettingSliderItem(
                title = "Refresh time",
                subTitle = "Set the time (in minutes) between each refresh",
                steps = 6,
                range = 0f..6f,
                stringValues = listOf("Never", "5min", "10min", "15min", "20min", "25min", "30min"),
                initialValue = 1f
            ) {

            }
            HorizontalDivider()
        }
        item {
            SettingToggleItem(
                title = "Use new UI",
                isBetaFeature = true,
                subTitle = "Use the new UI. Requires restart to be applied.",
                initialValue = false
            ) {

            }
            HorizontalDivider()
        }
        item {
            SettingToggleItem(
                title = "Preload Notices",
                subTitle = "Immediately load Notices, before opening the Notices tab, to have them immediately ready",
                initialValue = false
            ) {

            }
            HorizontalDivider()
        }
        item {
            SettingToggleItem(
                title = "Cache search results",
                subTitle = "When enabled, the app keeps a local copy of the search options to provide a faster searching experience.\nIf disabled, expect to wait a couple of seconds before being able to search.",
                initialValue = true
            ) {

            }
            HorizontalDivider()
        }
    }
}
