package com.branhamplayer.android.data.database.metadata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface MetadataDao {

    @Query("DELETE FROM metadata")
    fun deleteAll(): Completable

    @Query("SELECT * FROM metadata LIMIT 1")
    fun fetchFirstIfExists(): Maybe<MetadataEntity>

    @Query("SELECT * FROM metadata WHERE id = :sermonId")
    fun fetchMetadata(sermonId: String): Maybe<MetadataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(metadata: List<MetadataEntity>): Completable
}
