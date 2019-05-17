package com.branhamplayer.android.data.dagger

import android.content.Context
import com.branhamplayer.android.data.database.BranhamPlayerDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule(private val context: Context) {

    @Provides
    fun provideBranhamPlayerDatabase() = BranhamPlayerDatabase.newInstance(context).build()
}
