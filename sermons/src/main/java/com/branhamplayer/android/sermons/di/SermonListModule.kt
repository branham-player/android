package com.branhamplayer.android.sermons.di

import android.content.Context
import com.branhamplayer.android.sermons.adapters.SermonListAdapter
import com.branhamplayer.android.sermons.mappers.SermonListMapper
import com.branhamplayer.android.sermons.reducers.SermonListReducer
import com.branhamplayer.android.sermons.repositories.SermonListRepository
import dagger.Module
import dagger.Provides

@Module
class SermonListModule(private val context: Context) {

    @Provides
    fun provideSermonListAdapter() = SermonListAdapter(context)

    @Provides
    fun provideSermonListReducer(
        sermonListMapper: SermonListMapper,
        repository: SermonListRepository
    ) = SermonListReducer(sermonListMapper, repository)

    @Provides
    fun provideSermonListMapper() = SermonListMapper()

    @Provides
    fun provideSermonListRepository() = SermonListRepository()
}
