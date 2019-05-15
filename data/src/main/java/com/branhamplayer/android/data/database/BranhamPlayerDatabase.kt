package com.branhamplayer.android.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.branhamplayer.android.data.DataConstants
import com.branhamplayer.android.data.database.metadata.MetadataDao
import com.branhamplayer.android.data.database.metadata.MetadataEntity

@Database(entities = [MetadataEntity::class], version = DataConstants.Database.version, exportSchema = false)
abstract class BranhamPlayerDatabase : RoomDatabase() {

    abstract fun metadataDao(): MetadataDao

    companion object {
        fun newInstance(context: Context) = Room.databaseBuilder(
            context,
            BranhamPlayerDatabase::class.java,
            DataConstants.Database.name
        )
    }
}
