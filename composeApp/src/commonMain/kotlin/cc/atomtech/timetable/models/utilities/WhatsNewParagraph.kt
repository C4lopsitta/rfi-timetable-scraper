package cc.atomtech.timetable.models.utilities

import kotlinx.serialization.Serializable

@Serializable
data class WhatsNewParagraph(
    val title: String,
    val content: List<String>
)
