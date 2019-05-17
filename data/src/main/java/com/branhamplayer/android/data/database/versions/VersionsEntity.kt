package com.branhamplayer.android.data.database.versions

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.branhamplayer.android.data.DataConstants

@Entity(tableName = DataConstants.Database.Tables.versions, indices = [Index(value = ["property"], unique = true)])
data class VersionsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val property: String,
    val version: String
)
