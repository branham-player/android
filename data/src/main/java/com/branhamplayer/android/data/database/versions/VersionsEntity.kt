package com.branhamplayer.android.data.database.versions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.branhamplayer.android.data.DataConstants

@Entity(tableName = DataConstants.Database.Tables.versions)
data class VersionsEntity(
    @PrimaryKey(autoGenerate = DataConstants.Database.Tables.Versions.autoGenerateId) val id: Int = 0,
    @ColumnInfo(name = DataConstants.Database.Tables.Versions.property) val property: String,
    @ColumnInfo(name = DataConstants.Database.Tables.Versions.version) val version: String
)
