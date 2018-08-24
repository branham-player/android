package tech.oliver.branhamplayer.filelist

import java.util.Date

data class FileListViewModel(
        val date: Date?,
        val formattedDate: String?,
        val name: String,
        val path: String
)
