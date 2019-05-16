package com.branhamplayer.android.data.database.metadata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.branhamplayer.android.data.DataConstants
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface MetadataDao {

    @Query("DELETE FROM ${DataConstants.Database.Tables.metadata}")
    fun deleteAll(): Completable

    @Query("SELECT * FROM ${DataConstants.Database.Tables.metadata} WHERE ${DataConstants.Database.Tables.Metadata.id} = :sermonId")
    fun fetchMetadata(sermonId: String): Maybe<MetadataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(metadata: List<MetadataEntity>): Completable
}
