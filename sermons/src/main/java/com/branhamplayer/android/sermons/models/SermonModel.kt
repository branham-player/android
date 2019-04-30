package com.branhamplayer.android.sermons.models

import com.branhamplayer.android.base.Model
import java.util.Date

data class SermonModel(
        val artist: String,
        val date: Date?,
        val formattedDate: String,
        val name: String,
        val path: String
) : Model
