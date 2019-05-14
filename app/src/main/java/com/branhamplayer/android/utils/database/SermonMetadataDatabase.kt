package com.branhamplayer.android.utils.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.branhamplayer.android.StartupConstants
import com.branhamplayer.android.utils.database.metadata.MetadataDao
import com.branhamplayer.android.utils.database.metadata.MetadataEntity

@Database(entities = [MetadataEntity::class], version = StartupConstants.Database.version, exportSchema = false)
abstract class SermonMetadataDatabase : RoomDatabase() {

    abstract fun metadataDao(): MetadataDao

    companion object {
        fun newInstance(context: Context) = Room.databaseBuilder(
            context,
            SermonMetadataDatabase::class.java,
            StartupConstants.Database.name
        )
    }
}
