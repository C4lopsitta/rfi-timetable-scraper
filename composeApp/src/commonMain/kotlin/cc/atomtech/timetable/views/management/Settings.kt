package cc.atomtech.timetable.views.management

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import cc.atomtech.timetable.AppVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.debugRunStrikesNotificationService
import cc.atomtech.timetable.enumerations.Platform
import cc.atomtech.timetable.platform
import cc.atomtech.timetable.toggleStrikesNotificationService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingSliderItem(
    title: String,
    subTitle: String? = null,
    steps: Int,
    range: ClosedFloatingPointRange<Float>,
    value: Float,
    stringValues: List<String>? = null,
    isNotAvailableFeature: Boolean = false,
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
                enabled = !isNotAvailableFeature,
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
                if(isBetaFeature && isNotAvailableFeature) Box( modifier = Modifier.width(2.dp) )
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
            enabled = !isNotAvailableFeature,
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
    var allowStationCaching by remember { mutableStateOf(true) }
    var useStrikesNotificationService by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        reloadDelayValue = preferences.getReloadDelay().first().toFloat()
        allowStationCaching = preferences.getStoreStations().first()
        useStrikesNotificationService = preferences.getUseStrikesNotificationService().first()
    }


    LazyColumn (
        modifier = Modifier.fillMaxSize().padding( end = 12.dp )
    ) {
        item {
            SettingSliderItem(
                title = StringRes.get("setting_refresh"),
                subTitle = StringRes.get("setting_refresh_desc"),
                steps = 6,
                range = 0f..6f,
                stringValues = listOf(
                    StringRes.get("never"),
                    StringRes.format("int_mins", "5"),
                    StringRes.format("int_mins", "10"),
                    StringRes.format("int_mins", "15"),
                    StringRes.format("int_mins", "20"),
                    StringRes.format("int_mins", "25"),
                    StringRes.format("int_mins", "30")
                ),
                value = reloadDelayValue
            ) { updateValue {
                reloadDelayValue = it
                preferences.setReloadDelay(it.roundToInt())
            } }
            HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
        }
        item {
            SettingToggleItem(
                title = StringRes.get("setting_cache_search_results"),
                subTitle = StringRes.get("setting_cache_search_results_desc"),
                value = allowStationCaching
            ) {
                allowStationCaching = it
                updateValue {
                    preferences.setStoreStations(it)
                    if(!allowStationCaching) preferences.setStationCache("")
                }
            }
            HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
        }

        if(platform == Platform.ANDROID) {
            item {
                SettingToggleItem(
                    title = StringRes.get("setting_strike_notification_service"),
                    subTitle = StringRes.get("setting_strike_notification_service_desc"),
                    isBetaFeature = true,
                    value = useStrikesNotificationService
                ) {
                    updateValue {
                        useStrikesNotificationService = !useStrikesNotificationService
                        preferences.setUseStrikesNotificationService(useStrikesNotificationService)

                        toggleStrikesNotificationService(useStrikesNotificationService)
                    }
                }

                OutlinedButton(
                    content = { Text("Run") },
                    onClick = { debugRunStrikesNotificationService() }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            }
        }

        item {
            SettingToggleItem(
                title = StringRes.get("setting_preload_notices"),
                subTitle = StringRes.get("setting_preload_notices_desc"),
                isNotAvailableFeature = true,
                value = false
            ) {

            }
            HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
        }

        item {
            Column (
                modifier = Modifier.fillMaxWidth().padding( vertical = 12.dp ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("${StringRes.get("app_name")} - cc.atomtech.timetable")
                Text("Application licensed under GNU GPLv3 License")
                Text("App Version ${AppVersion.versionName} (${AppVersion.versionCode})")
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
