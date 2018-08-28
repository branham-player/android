package tech.oliver.branhamplayer.android.sermons.database.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.oliver.branhamplayer.android.sermons.SermonConstants

@Entity(tableName = SermonConstants.Database.Tables.Recent)
data class RecentEntity(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "media_id") var mediaId: String = ""
)
