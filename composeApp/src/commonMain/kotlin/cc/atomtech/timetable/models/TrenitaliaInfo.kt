package cc.atomtech.timetable.models

class TrenitaliaInfo(
    val isTrafficRegular: Boolean,
    val extraEvents: List<TrenitaliaEventDetails> = listOf(),
    val irregularTrafficEvents: List<TrenitaliaEventDetails> = listOf(),
    val infoLavori: List<TrenitaliaInfoLavori>
)