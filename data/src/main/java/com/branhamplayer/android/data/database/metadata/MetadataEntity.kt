package com.branhamplayer.android.data.database.metadata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.branhamplayer.android.data.DataConstants

@Entity(tableName = DataConstants.Database.Tables.metadata)
data class MetadataEntity(
    @PrimaryKey(autoGenerate = DataConstants.Database.Tables.Metadata.autoGenerateId) @ColumnInfo(name = DataConstants.Database.Tables.Metadata.id) val id: String,
    @ColumnInfo(name = "audio") val audio: String,
    @ColumnInfo(name = "artwork_large") val artworkLarge: String,
    @ColumnInfo(name = "artwork_thumbnail") val artworkThumbnail: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subtitle") val subtitle: String
)
