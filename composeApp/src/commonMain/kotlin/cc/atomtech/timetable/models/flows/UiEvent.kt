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
    data class RfiTimetableScrapingException(val ex: Exception) : UiEvent()

    /**
     * A data class used to communicate Station Loading errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @author Simone Robaldo
     */
    data class StationLoadingException(val ex: Exception) : UiEvent()

    /**
     * A data class used to communicate Trenitalia Infolavori errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @author Simone Robaldo
     */
    data class TrenitaliaInfolavoriException(val ex: Exception) : UiEvent()

    /**
     * A data class used to communicate RFI Feed errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @author Simone Robaldo
     */
    data class RfiFeedException(val ex: Exception) : UiEvent()

    /**
     * A data class used to communicate Trenitalia Cercatreno errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @author Simone Robaldo
     */
    data class TrenitaliaCercatrenoException(val ex: Exception) : UiEvent()

    /**
     * A data class used to communicate generic errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @author Simone Robaldo
     */
    data class GenericException(val ex: Exception) : UiEvent()

    // TODO)) Add Feeds, Trenitalia Announcements and others to the UiEvents
}
