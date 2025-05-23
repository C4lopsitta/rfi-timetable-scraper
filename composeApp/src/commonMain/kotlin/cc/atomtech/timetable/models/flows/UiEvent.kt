package cc.atomtech.timetable.models.flows


/**
 * A Sealed Class used by the ViewModels to communicate with the UI.
 *
 * @since 1.5.0
 * @author Simone Robaldo
 */
sealed class UiEvent {
    /**
     * A data class used to communicate Timetable (RFI Partenze/Arrivi) fetching errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @author Simone Robaldo
     */
    data class RfiTimetableScrapingException(val ex: Exception): UiEvent()

    // TODO)) Add Feeds, Trenitalia Announcements and others to the UiEvents
}
