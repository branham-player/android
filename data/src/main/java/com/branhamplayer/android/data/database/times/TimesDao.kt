package com.branhamplayer.android.data.database.times

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface TimesDao {

    @Query("SELECT * FROM times WHERE media_id = :mediaId")
    fun fetchSermon(mediaId: String): Maybe<TimesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(time: TimesEntity)
}
