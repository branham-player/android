package tech.oliver.branhamplayer.filelist

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

class FileListMapper {
    fun map(files: List<File>) = files.map {
        FileListViewModel(
                date = buildDate(it.name),
                formattedDate = formatDate(it.name),
                name = trimMetadata(it.nameWithoutExtension),
                path = it.absolutePath
        )
    }.sortedWith(compareBy<FileListViewModel, Date?>(nullsLast()) {
        it.date
    }.thenBy {
        it.name
    })

    private fun buildDate(name: String): Date? {
        val canFormat = DATE_PATTERN.toRegex()

        if (!canFormat.containsMatchIn(name)) {
            return null
        }

        val month = name.substring(3, 5).toInt() - 1
        val day = name.substring(5, 7).toInt()
        val year = name.substring(0, 2).toInt() + 1900

        return GregorianCalendar(year, month, day).time
    }

    private fun formatDate(name: String): String? {
        val date = buildDate(name)

        return try {
            SimpleDateFormat("MMMM d, yyyy", Locale.US).format(date)
        } catch (e: Exception) {
            null
        }
    }

    private fun trimMetadata(name: String): String {
        var output = name

        val replaceDate = DATE_PATTERN.toRegex()
        output = replaceDate.replace(output, "")

        val replaceVGR = VGR_PATTERN.toRegex()
        output = replaceVGR.replace(output, "")

        return output.trim()
    }

    companion object {
        const val DATE_PATTERN = "^\\d{2}-\\d{4}[AEM]?"
        const val VGR_PATTERN = "VGR"
    }
}
