package com.branhamplayer.android.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.branhamplayer.android.data.DataConstants
import com.branhamplayer.android.data.database.metadata.MetadataDao
import com.branhamplayer.android.data.database.metadata.MetadataEntity
import com.branhamplayer.android.data.database.versions.VersionsDao
import com.branhamplayer.android.data.database.versions.VersionsEntity

@Database(
    entities = [MetadataEntity::class, VersionsEntity::class],
    version = DataConstants.Database.version,
    exportSchema = false
)
abstract class BranhamPlayerDatabase : RoomDatabase() {

    abstract fun metadataDao(): MetadataDao
    abstract fun versionsDao(): VersionsDao

    companion object {
        fun newInstance(context: Context) = Room.databaseBuilder(
            context,
            BranhamPlayerDatabase::class.java,
            DataConstants.Database.name
        )
    }
}
