package com.branhamplayer.android.data.database.versions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.branhamplayer.android.data.DataConstants
import io.reactivex.Maybe

@Dao
interface VersionsDao {

    @Query("SELECT * FROM ${DataConstants.Database.Tables.versions} WHERE ${DataConstants.Database.Tables.Versions.property} = '${DataConstants.Database.Tables.Versions.metadataVersion}'")
    fun fetchMetadataVersion(): Maybe<VersionsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(versionInfo: VersionsEntity)
}
