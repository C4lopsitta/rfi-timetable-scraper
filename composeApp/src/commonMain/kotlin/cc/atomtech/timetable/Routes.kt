package cc.atomtech.timetable

sealed class Page( val route: String ) {
    data object Home: Page("home")
    data object Station: Page("station") {
        data object Departures: Page("departures")
        data object Arrivals: Page("arrivals")
        data object Schedule: Page("schedule")
        data object Info: Page("info")
        data object TrainDetails: Page("train/{number}") {
            fun createRoute(number: String) = "train/$number"
        }
    }
    data object Notices: Page("notices") {
        data object Trenitalia: Page("trenitalia")
        data object Live: Page("live")
        data object Announcements: Page("announcements")
    }
    data object Settings: Page("settings")
}

object Routes {
    const val HOME_DEPARTURES = "home"
    const val FAVOURITES = "favourites"
    const val SETTINGS = "settings"
    const val SEARCH = "search"

    const val STATION = "station"
    const val DEPARTURES = "station/departures"
    const val ARRIVALS = "station/arrivals"
    const val SCHEDULE = "station/schedule"
    const val STATION_INFO = "station/info"
    const val TRAIN_DETAILS = "station/train"

    const val INFOLAVORI = "infolavori"
    const val TRENITALIA = "infolavori/trenitalia"
    const val LIVE = "infolavori/live"
    const val NOTICES = "infolavori/notices"

    const val APP_INFO = "info"
}
