package com.branhamplayer.android.data.database.versions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.branhamplayer.android.data.DataConstants
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface VersionsDao {

    @Query("SELECT * FROM versions WHERE property = '${DataConstants.Database.Tables.Metadata.metadataVersion}'")
    fun fetchMetadataVersion(): Maybe<VersionsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(versionInfo: VersionsEntity): Completable
}
