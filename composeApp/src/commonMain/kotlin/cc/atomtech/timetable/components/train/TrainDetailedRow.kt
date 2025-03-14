package cc.atomtech.timetable.components.train

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.rfi.TrainData

@Composable
fun TrainDetailedRow(
    trainData: TrainData,
    inDummyMode: Boolean = false
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                role  = Role.Button,
                onClickLabel = StringRes.get("click_for_details"),
                onClick = {
                    if(inDummyMode) return@clickable
                    TODO()
                }
            )
    ) {

    }
}