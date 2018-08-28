package tech.oliver.branhamplayer.android.sermons.database.recent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface RecentDao {

    @Query("SELECT * FROM recent LIMIT 1")
    fun fetchSermon(): Maybe<RecentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(recent: RecentEntity)
}
