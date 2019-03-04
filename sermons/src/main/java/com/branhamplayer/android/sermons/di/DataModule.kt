package com.branhamplayer.android.sermons.di

import android.content.Context
import com.branhamplayer.android.sermons.mappers.SermonMapper
import com.branhamplayer.android.sermons.reducers.DataReducer
import com.branhamplayer.android.sermons.repositories.SermonsRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideDataReducer(context: Context, mapper: SermonMapper, repository: SermonsRepository) =
        DataReducer(context, mapper, repository)

    @Provides
    fun provideSermonMapper() = SermonMapper()

    @Provides
    fun provideSermonsRepository() = SermonsRepository()
}