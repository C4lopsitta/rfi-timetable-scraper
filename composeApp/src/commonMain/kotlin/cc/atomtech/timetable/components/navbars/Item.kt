package cc.atomtech.timetable.components.navbars

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconDescriptor: String,
    val route: String
)
