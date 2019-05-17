package com.branhamplayer.android.data.network

import com.branhamplayer.android.data.DataConstants
import com.branhamplayer.android.data.models.RawMetadata
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RawMetadataNetworkProvider {
    companion object {
        val api: Retrofit = Retrofit.Builder()
            .baseUrl(DataConstants.Network.RawMetadata.baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Headers("Accept: application/json")
    @GET(DataConstants.Network.RawMetadata.path)
    fun getRawMetadata(@Path(value = "version") version: String?): Single<List<RawMetadata>>
}
