package cc.atomtech.timetable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cc.atomtech.timetable.StringRes
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
private fun isCurrentRoute(navController: NavHostController, route: String): Boolean {
    return navController.currentBackStackEntryAsState().value?.destination?.route?.contains(route) ?: false
}

@Composable
private fun currentRouteContains(navController: NavHostController, contains: String): Boolean {
    return navController.currentBackStackEntryAsState().value?.destination?.route?.contains(contains) ?: false
}

@Composable
private fun HighlightedIcon(icon: @Composable () -> Unit, highlighed: @Composable () -> Unit, condition: Boolean) {
    if( condition ) {
        icon()
    } else {
        highlighed()
    }
}

@Composable
fun NavRail(navController: NavHostController,
            triggerReload: () -> Unit) {

    NavigationRail (
        modifier = Modifier.fillMaxHeight(),
    ) {
        Column (
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // region topHalf
            Column {
                if (currentRouteContains(navController, "details/")) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = StringRes.get("back")
                        )
                    }
                } else {
                    Icon(
                        contentDescription = StringRes.get("app_name"),
                        imageVector = Icons.Rounded.Train,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
                HorizontalDivider(modifier = Modifier.width(32.dp).padding(bottom = 12.dp))
                NavigationRailItem(
                    label = { Text(StringRes.get("nav_departures")) },
                    icon = {
                        val isCurrentRoute = isCurrentRoute(navController, "departures")
                        HighlightedIcon(
                            {
                                Icon(
                                    Icons.Rounded.ArrowUpward,
                                    contentDescription = StringRes.get("nav_departures")
                                )
                            },
                            {
                                Icon(
                                    Icons.Filled.ArrowUpward,
                                    contentDescription = StringRes.get("nav_departures")
                                )
                            },
                            isCurrentRoute
                        )
                    },
                    selected = isCurrentRoute(navController, "departures"),
                    onClick = {
                        navController.navigate("departures")
                    }
                )
                Spacer(Modifier.height(12.dp))
                NavigationRailItem(
                    label = { Text(StringRes.get("nav_arrivals")) },
                    icon = {
                        val isCurrentRoute = isCurrentRoute(navController, "arrivals")
                        HighlightedIcon(
                            {
                                Icon(
                                    Icons.Rounded.ArrowDownward,
                                    contentDescription = StringRes.get("nav_arrivals")
                                )
                            },
                            {
                                Icon(
                                    Icons.Filled.ArrowDownward,
                                    contentDescription = StringRes.get("nav_arrivals")
                                )
                            },
                            isCurrentRoute
                        )
                    },
                    selected = isCurrentRoute(navController, "arrivals"),
                    onClick = {
                        navController.navigate("arrivals")
                    }
                )
                Spacer(Modifier.height(12.dp))
                NavigationRailItem(
                    label = { Text(StringRes.get("nav_works")) },
                    icon = {
                        val isCurrentRoute = isCurrentRoute(navController, "infolavori")
                        HighlightedIcon(
                            {
                                Icon(
                                    Icons.Rounded.Engineering,
                                    contentDescription = StringRes.get("nav_works")
                                )
                            },
                            {
                                Icon(
                                    Icons.Filled.Engineering,
                                    contentDescription = StringRes.get("nav_works")
                                )
                            },
                            isCurrentRoute
                        )
                    },
                    selected = isCurrentRoute(navController, "infolavori"),
                    onClick = {
                        navController.navigate("infolavori")
                    }
                )
                Spacer(Modifier.height(12.dp))
                NavigationRailItem(
                    label = { Text("Info") },
                    icon = {
                        val isCurrentRoute = isCurrentRoute(navController, "appinfo")
                        HighlightedIcon(
                            { Icon(Icons.Rounded.Info, contentDescription = "Info") },
                            { Icon(Icons.Filled.Info, contentDescription = "Info") },
                            isCurrentRoute
                        )
                    },
                    selected = isCurrentRoute(navController, "info"),
                    onClick = {
                        navController.navigate("info")
                    }
                )
                Spacer(Modifier.height(12.dp))
            }
            // endregion topHalf

            // region bottomHalf
            Column {
                NavigationRailItem(
                    label = { Text(StringRes.get("search")) },
                    icon = {
                        val isCurrentRoute = isCurrentRoute(navController, "search")
                        HighlightedIcon(
                            {
                                Icon(
                                    Icons.Rounded.Search,
                                    contentDescription = StringRes.get("search")
                                )
                            },
                            {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = StringRes.get("search")
                                )
                            },
                            isCurrentRoute
                        )
                    },
                    selected = isCurrentRoute(navController, "search"),
                    onClick = {
                        navController.navigate("search")
                    }
                )
                Spacer(Modifier.height(12.dp))
                NavigationRailItem(
                    label = { Text(StringRes.get("reload")) },
                    icon = {
                        Icon(
                            Icons.Rounded.Refresh,
                            contentDescription = StringRes.get("reload")
                        )
                    },
                    selected = false,
                    onClick = {
                        triggerReload()
                    }
                )
                Spacer(Modifier.height(12.dp))
                NavigationRailItem(
                    label = { Text(StringRes.get("nav_favourites")) },
                    icon = {
                        val isCurrentRoute = isCurrentRoute(navController, "favourites")
                        HighlightedIcon(
                            {
                                Icon(
                                    Icons.Rounded.Star,
                                    contentDescription = StringRes.get("nav_favourites")
                                )
                            },
                            {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = StringRes.get("nav_favourites")
                                )
                            },
                            isCurrentRoute
                        )
                    },
                    selected = isCurrentRoute(navController, "favourites"),
                    onClick = {
                        navController.navigate("favourites")
                    }
                )
            }

            // endregion bottomHalf

//            HorizontalDivider(modifier = Modifier.width(32.dp).padding(vertical = 12.dp))

        }
    }
}