package cc.atomtech.timetable.models

class TrenitaliaInfoLavori(
    val regionName: String,
    val issues: List<TrenitaliaEventDetails>,
    val busServiceLink: String?,
    val worksAndServiceModificationsLink: String?
)