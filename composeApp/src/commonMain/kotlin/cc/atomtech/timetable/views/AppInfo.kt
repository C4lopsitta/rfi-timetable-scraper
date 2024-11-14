package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import my.project.BuildConfig

@Composable
fun AppInfo() {
    val versionNumber = if(BuildConfig.PROJECT_VERSION != "unspecified") BuildConfig.PROJECT_VERSION else "1.3.0"

    Column {
        Text("Timetable", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Text("cc.atomtech.timetable", fontSize = 8.sp, fontWeight = FontWeight.Light)
        Text("Application licensed under GNU GPLv3.")
        Text("Version ${versionNumber}")
    }
}
