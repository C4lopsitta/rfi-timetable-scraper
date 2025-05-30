package cc.atomtech.timetable.extenions


/** Extension function that converts a shittily formatted RFI/Trenitalia Station name into a well formatted one.
 *
 * @return [String] A well-formatted station name
 * @since 1.0.0
 * @author Simone Robaldo
 */
fun String.toStationName(): String {
    return this.lowercase()
        .replace("''''", "'")
        .split(" ")
        .joinToString(" ") { word ->
            word.split(".")
                .joinToString(".") { part ->
                    part.replaceFirstChar { it.uppercaseChar() }
                }
        }
        .split(" ")
        .joinToString(" ") { word ->
            word.split("-")
                .joinToString("-") { part ->
                    part.replaceFirstChar { it.uppercaseChar() }
                }
        }
        .replace("-", " - ")
}
