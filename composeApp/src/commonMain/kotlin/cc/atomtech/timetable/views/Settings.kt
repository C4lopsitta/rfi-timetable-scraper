package cc.atomtech.timetable.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import cc.atomtech.timetable.StringRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingSliderItem(
    title: String,
    subTitle: String? = null,
    steps: Int,
    range: ClosedFloatingPointRange<Float>,
    value: Float,
    stringValues: List<String>? = null,
    onValueChange: (Float) -> Unit
) {
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
                    stringValues[value.roundToInt()]
                } else { "${value.roundToInt()}" },
                modifier = Modifier.padding( horizontal = 8.dp )
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = {
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
    isNotAvailableFeature: Boolean = false,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding( vertical = 8.dp )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                role  = Role.Switch,
                onClick =  {
                    onValueChange(!value)
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
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text("Beta", modifier = Modifier.padding( horizontal = 8.dp ))
                }
                if(isNotAvailableFeature) Card (
                    modifier = Modifier.padding( vertical = 4.dp ),
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(StringRes.get("feature_not_available"), modifier = Modifier.padding( horizontal = 8.dp ))
                }
                Text(title, fontSize = 20.sp, modifier = Modifier.padding( start = if(isBetaFeature || isNotAvailableFeature) 8.dp else 0.dp ))
            }
            if (subTitle != null) Text(subTitle)
        }
        Switch(
            checked = value,
            modifier = Modifier.width(64.dp),
            onCheckedChange = {
                onValueChange(!value)
            }
        )
    }

}

fun updateValue(lambda: suspend () -> Unit) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
        lambda()
    }
}

@Composable
fun Settings(
    preferences: AppPreferences
) {
    var reloadDelayValue by remember { mutableStateOf(1f) }
    var useNewUi by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        reloadDelayValue = preferences.getReloadDelay().first().toFloat()
        useNewUi = preferences.getUseNewUi().first()
    }


    LazyColumn (
        modifier = Modifier.fillMaxSize().padding( 12.dp )
    ) {
        item {
            SettingSliderItem(
                title = StringRes.get("setting_refresh"),
                subTitle = StringRes.get("setting_refresh_desc"),
                steps = 6,
                range = 0f..6f,
                stringValues = listOf(StringRes.get("never"), "5min", "10min", "15min", "20min", "25min", "30min"),
                value = reloadDelayValue
            ) { updateValue {
                reloadDelayValue = it
                preferences.setReloadDelay(it.roundToInt())
            } }
            HorizontalDivider()
        }
        item {
            SettingToggleItem(
                title = StringRes.get("setting_new_ui"),
                isBetaFeature = true,
                subTitle = StringRes.get("setting_new_ui_desc"),
                value = useNewUi
            ) { updateValue {
                useNewUi = it
                preferences.setUseNewUi(it)
            } }
            HorizontalDivider()
        }
        item {
            SettingToggleItem(
                title = StringRes.get("setting_preload_notices"),
                subTitle = StringRes.get("setting_preload_notices_desc"),
                isNotAvailableFeature = true,
                value = false
            ) {

            }
            HorizontalDivider()
        }
        item {
            SettingToggleItem(
                title = StringRes.get("setting_cache_search_results"),
                subTitle = StringRes.get("setting_cache_search_results_desc"),
                isNotAvailableFeature = true,
                value = true
            ) {

            }
            HorizontalDivider()
        }
        item {
            Column (
                modifier = Modifier.fillMaxWidth().padding( vertical = 12.dp ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("${StringRes.get("app_name")} - cc.atomtech.timetable")
                Text("Application licensed under GNU GPLv3 License")
                Row {
                    val uriHandler = LocalUriHandler.current

                    OutlinedButton(
                        content = {
                            Text("GitHub")
                            Icon(
                                Icons.AutoMirrored.Rounded.OpenInNew,
                                contentDescription = StringRes.get("open_in_browser")
                            )
                        },
                        onClick = {
                            uriHandler.openUri("https://github.com/C4lopsitta/rfi-timetable-scraper")
                        }
                    )
                    OutlinedButton(
                        modifier = Modifier.padding( start = 16.dp ),
                        content = {
                            Text("Web site")
                            Icon(
                                Icons.AutoMirrored.Rounded.OpenInNew,
                                contentDescription = StringRes.get("open_in_browser")
                            )
                        },
                        onClick = {
                            uriHandler.openUri("https://github.com/C4lopsitta/rfi-timetable-scraper")
                        }
                    )
                }
            }
        }
    }
}
