package com.branhamplayer.android.data.database.metadata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.branhamplayer.android.data.DataConstants

@Entity(tableName = DataConstants.Database.Tables.metadata)
data class MetadataEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val audio: String,
    @ColumnInfo(name = "artwork_large") val artworkLarge: String,
    @ColumnInfo(name = "artwork_thumbnail") val artworkThumbnail: String,
    val title: String,
    val subtitle: String
)
