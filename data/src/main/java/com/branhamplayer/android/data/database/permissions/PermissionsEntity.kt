package com.branhamplayer.android.data.database.permissions

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.branhamplayer.android.data.DataConstants

@Entity(tableName = DataConstants.Database.Tables.permissions)
data class PermissionsEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val permission: String
)
