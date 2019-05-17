package com.branhamplayer.android.data.dagger

import com.branhamplayer.android.data.mappers.MetadataMapper
import dagger.Module
import dagger.Provides

@Module
class MapperModule {

    @Provides
    fun provideMetadataMapper() = MetadataMapper()
}
