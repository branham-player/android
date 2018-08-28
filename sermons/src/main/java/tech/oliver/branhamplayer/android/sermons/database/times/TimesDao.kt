package tech.oliver.branhamplayer.android.sermons.database.times

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Maybe

@Dao
interface TimesDao {

    @Query("SELECT * FROM times WHERE media_id = :mediaId")
    fun fetchSermon(mediaId: String): Maybe<TimesEntity>

    @Update
    fun update(time: TimesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(time: TimesEntity)
}
