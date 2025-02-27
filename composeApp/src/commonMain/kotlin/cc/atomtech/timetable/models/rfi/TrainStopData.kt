package cc.atomtech.timetable.models.rfi

data class TrainStopData(
    val name: String,
    val time: String,
    val isCurrentStop: Boolean
)
