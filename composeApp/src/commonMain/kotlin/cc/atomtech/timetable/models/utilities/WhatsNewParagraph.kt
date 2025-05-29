package cc.atomtech.timetable.models.utilities

import kotlinx.serialization.Serializable

/**
 * Handles the Paragraphs from the String Resource viewed in the [cc.atomtech.timetable.views.management.WhatsNew] UI.
 *
 * @since 1.5.0
 * @author Simone Robaldo
 */
@Serializable
data class WhatsNewParagraph(
    val title: String,
    val content: List<String>
)
