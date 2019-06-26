package com.branhamplayer.android.data.dagger

import com.branhamplayer.android.data.network.RawMetadataNetworkProvider
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideRawMetadataNetworkProvider(): RawMetadataNetworkProvider =
        RawMetadataNetworkProvider.api.create(RawMetadataNetworkProvider::class.java)
}
