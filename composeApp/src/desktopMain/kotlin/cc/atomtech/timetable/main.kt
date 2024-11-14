package cc.atomtech.timetable

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.rememberNavController

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Timetables"
    ) {
        val navController = rememberNavController()
        Main(navController, isDesktop = true)
    }
}