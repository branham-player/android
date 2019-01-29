package com.branhamplayer.android.sermons.database.times

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.branhamplayer.android.sermons.SermonConstants

@Entity(tableName = SermonConstants.Database.Tables.Times, indices = [Index(value = ["media_id"], unique = true)])
data class TimesEntity(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "media_id") var mediaId: String,
        @ColumnInfo(name = "time") var time: Int
)
