package cc.atomtech.timetable.models

data class TrainStop (
    val name: String,
    val time: String,
    val isCurrentStop: Boolean
)
