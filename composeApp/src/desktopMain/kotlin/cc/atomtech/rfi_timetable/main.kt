package cc.atomtech.rfi_timetable

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "rfi",
    ) {
        App()
    }
}