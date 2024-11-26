package cc.atomtech.timetable.models

import cc.atomtech.timetable.components.TrenitaliaIrregularTrafficDetails

class TrenitaliaInfo(
    val isTrafficRegular: Boolean,
    val irregularTrafficEvents: List<TrenitaliaIrregularTrafficDetails> = listOf(),
    val infoLavori: List<TrenitaliaInfoLavori>
) {

}