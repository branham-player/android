package com.branhamplayer.android.dagger.modules

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.branhamplayer.android.data.database.BranhamPlayerDatabase
import com.branhamplayer.android.data.mappers.MetadataMapper
import com.branhamplayer.android.data.network.RawMetadataNetworkProvider
import com.branhamplayer.android.middleware.PreflightChecklistMiddleware
import com.branhamplayer.android.reducers.PreflightChecklistReducer
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class PreflightChecklistModule(private val context: Context) {

    @Provides
    fun provideAlertDialogBuilder() = AlertDialog.Builder(context)

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Provides
    fun providePreflightChecklistMiddleware(
        firebaseRemoteConfig: FirebaseRemoteConfig,
        branhamPlayerDatabase: BranhamPlayerDatabase,
        rawMetadataNetworkProvider: RawMetadataNetworkProvider,
        metadataMapper: MetadataMapper,
        @Named(RxJavaModule.BG) bg: Scheduler,
        @Named(RxJavaModule.UI) ui: Scheduler
    ) = PreflightChecklistMiddleware(
        firebaseRemoteConfig,
        branhamPlayerDatabase,
        rawMetadataNetworkProvider,
        metadataMapper,
        bg,
        ui
    )

    @Provides
    fun providePreflightChecklistReducer() = PreflightChecklistReducer()
}
