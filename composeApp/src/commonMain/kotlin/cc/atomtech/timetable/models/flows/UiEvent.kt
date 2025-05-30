package cc.atomtech.timetable.models.flows

import cc.atomtech.timetable.enumerations.ui.UiEventSeverity


/**
 * A Sealed Class used by the ViewModels to communicate with the UI.
 *
 * @since 1.5.0
 * @author Simone Robaldo
 */
sealed class UiEvent(
    open val ex: Exception
) {
    /**
     * A data class used to communicate Timetable (RFI Partenze/Arrivi) fetching errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class RfiTimetableScrapingException(
        override val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent(ex)

    /**
     * A data class used to communicate Station Loading errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class StationLoadingException(
        override val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent(ex)

    /**
     * A data class used to communicate Trenitalia Infolavori errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class TrenitaliaInfolavoriException(
        override val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent(ex)

    /**
     * A data class used to communicate RFI Feed errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class RfiFeedException(
        override val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent(ex)

    /**
     * A data class used to communicate Trenitalia Cercatreno errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class TrenitaliaCercatrenoException(
        override val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent(ex)

    /**
     * A data class used to communicate generic errors to the UI.
     *
     * @param ex The exception that caused the error.
     * @param displayResId An optional stringRes ID to show in place of the Exception
     * @author Simone Robaldo
     */
    data class GenericException(
        override val ex: Exception,
        val severity: UiEventSeverity = UiEventSeverity.MEDIUM,
        val displayResId: String? = null
    ) : UiEvent(ex)
}
