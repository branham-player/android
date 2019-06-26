package com.branhamplayer.android.data.database.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.branhamplayer.android.data.DataConstants

@Entity(tableName = DataConstants.Database.Tables.recent)
data class RecentEntity(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "media_id") var mediaId: String
)
