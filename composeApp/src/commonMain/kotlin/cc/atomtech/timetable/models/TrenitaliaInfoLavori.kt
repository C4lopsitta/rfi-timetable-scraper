package cc.atomtech.timetable.models

import cc.atomtech.timetable.components.TrenitaliaEventDetails

class TrenitaliaInfoLavori(
    val regionName: String,
    val issues: List<TrenitaliaEventDetails>,
    val busServiceLink: String?,
    val worksAndServiceModificationsLink: String?
) {
}