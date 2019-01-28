package com.branhamplayer.android.sermons.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.branhamplayer.android.sermons.SermonConstants
import com.branhamplayer.android.sermons.database.recent.RecentDao
import com.branhamplayer.android.sermons.database.recent.RecentEntity
import com.branhamplayer.android.sermons.database.times.TimesDao
import com.branhamplayer.android.sermons.database.times.TimesEntity

@Database(entities = [RecentEntity::class, TimesEntity::class], version = SermonConstants.Database.Version, exportSchema = false)
abstract class SermonsDatabase : RoomDatabase() {

    abstract fun recentDao(): RecentDao
    abstract fun timesDao(): TimesDao

    // region Static Constructor

    companion object {
        fun newInstance(context: Context) =
                Room.databaseBuilder(context, SermonsDatabase::class.java, SermonConstants.Database.Name).build()
    }

    // endregion
}
