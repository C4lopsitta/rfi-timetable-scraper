package cc.atomtech.timetable.models.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Loaders (
    var departures: MutableState<Boolean> = mutableStateOf(false),
    var arrivals: MutableState<Boolean> = mutableStateOf(false),
    var notices: MutableState<Boolean> = mutableStateOf(false),
    var trenitaliaNotices: MutableState<Boolean> = mutableStateOf(false),
    var trenitaliaSpecificData: MutableState<Boolean> = mutableStateOf(false),

    var timetableTrigger: MutableState<Boolean> = mutableStateOf(false)
)
