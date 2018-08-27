package tech.oliver.branhamplayer.android.files.models

import tech.oliver.branhamplayer.android.base.Model
import java.util.Date

data class FileModel(
        val artist: String,
        val date: Date?,
        val formattedDate: String,
        val name: String,
        val path: String
) : Model
