package cc.atomtech.timetable.models.flows

import cc.atomtech.timetable.enumerations.ui.UiEventSeverity


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
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class RfiTimetableScrapingException(
        val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent()

    /**
     * A data class used to communicate Station Loading errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class StationLoadingException(
        val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent()

    /**
     * A data class used to communicate Trenitalia Infolavori errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class TrenitaliaInfolavoriException(
        val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent()

    /**
     * A data class used to communicate RFI Feed errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class RfiFeedException(
        val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent()

    /**
     * A data class used to communicate Trenitalia Cercatreno errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class TrenitaliaCercatrenoException(
        val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent()

    /**
     * A data class used to communicate generic errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class GenericException(
        val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent()
}
