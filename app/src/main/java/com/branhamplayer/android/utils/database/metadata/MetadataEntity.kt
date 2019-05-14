package com.branhamplayer.android.utils.database.metadata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.branhamplayer.android.StartupConstants

@Entity(tableName = StartupConstants.Database.Tables.metadata)
data class MetadataEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = "audio") val audio: String,
    @ColumnInfo(name = "artwork_large") val artworkLarge: String,
    @ColumnInfo(name = "artwork_thumbnail") val artworkThumbnail: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subtitle") val subtitle: String
)
