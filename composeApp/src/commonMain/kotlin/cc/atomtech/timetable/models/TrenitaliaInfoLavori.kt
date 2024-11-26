package cc.atomtech.timetable.models

import cc.atomtech.timetable.components.TrenitaliaIrregularTrafficDetails

class TrenitaliaInfoLavori(
    val regionName: String,
    val issues: List<TrenitaliaIrregularTrafficDetails>,
    val busServiceLink: String?,
    val worksAndServiceModificationsLink: String?
) {
}