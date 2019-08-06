package com.branhamplayer.android.dagger.modules

import android.content.Intent
import android.net.Uri
import com.branhamplayer.android.StartupConstants
import com.branhamplayer.android.ui.StartupActivity
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RoutingModule(private val startupActivity: StartupActivity) {

    companion object {
        const val GooglePlay = "GooglePlay"
        const val Sermons = "Sermons"
    }

    @Provides
    @Named(GooglePlay)
    fun provideGooglePlayIntent() = Intent(Intent.ACTION_VIEW, Uri.parse(StartupConstants.Intents.googlePlay))

    @Provides
    @Named(Sermons)
    fun provideSermonsIntent() = Intent(Intent.ACTION_VIEW, Uri.parse(StartupConstants.Intents.sermonsModule))
}
