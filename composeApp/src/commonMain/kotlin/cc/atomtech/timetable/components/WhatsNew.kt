package cc.atomtech.timetable.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.AppVersion
import cc.atomtech.timetable.StringRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsNew(
    isOpen: Boolean = false,
    toggleStatus: () -> Unit
) {
    val state = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = toggleStatus
    ) {
        LazyColumn {
            item {
                Text(
                    "${StringRes.get("app_name")} ${StringRes.format("version", AppVersion.versionName)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                Text(StringRes.get("whatsnew"))
            }
        }
    }
}
