package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.StringRes

/** Information about the app's version.
 */
@Deprecated("Unused and will be removed soon")
@Composable
fun AppInfo() {
    val versionNumber = "1.3.0"
    val uriHandler = LocalUriHandler.current

    Column {
        Text(StringRes.get("app_name"), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Text("cc.atomtech.timetable", fontSize = 8.sp, fontWeight = FontWeight.Light)
        Text("Application licensed under GNU GPLv3.")
        Text("Version ${StringRes.get("app_version")}")
        TextButton({
            uriHandler.openUri("https://raw.githubusercontent.com/C4lopsitta/rfi-timetable-scraper/refs/heads/main/privacy-policy")
        }) {
            Text("View Privacy Policy")
        }
    }
}
