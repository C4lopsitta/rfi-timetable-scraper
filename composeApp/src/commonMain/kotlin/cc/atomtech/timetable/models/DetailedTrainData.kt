package cc.atomtech.timetable.models

import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.enumerations.CurrentStationType
import cc.atomtech.timetable.enumerations.Operator

data class DetailedTrainData(
    val currentStationType: CurrentStationType,
    val departure: String,
    val arrival: String,
    val departsAt: String,
    val arrivesAt: String,
    val stops: List<TrainStop>,
    val delay: String?,
    val operator: Operator,
    val category: Category,
    val platform: String?,
    val details: String?,
    val number: String,
    val delayMinutes: Int
)

