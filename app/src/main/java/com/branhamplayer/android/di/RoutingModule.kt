package com.branhamplayer.android.di

import android.content.Intent
import android.net.Uri
import com.branhamplayer.android.StartupConstants
import com.branhamplayer.android.reducers.RoutingReducer
import com.branhamplayer.android.ui.AuthenticationFragment
import com.branhamplayer.android.ui.PreflightChecklistFragment
import com.branhamplayer.android.ui.StartupActivity
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RoutingModule {

    companion object {
        const val GooglePlay = "GooglePlay"
        const val Sermons = "Sermons"
    }

    @Provides
    @Named(GooglePlay)
    fun provideGooglePlayIntent() = Intent(Intent.ACTION_VIEW, Uri.parse(StartupConstants.Intents.googlePlay))

    @Provides
    fun provideRoutingReducer(
        startupActivity: StartupActivity,
        authenticationFragment: AuthenticationFragment,
        preflightChecklistFragment: PreflightChecklistFragment,
        @Named(GooglePlay) googlePlayIntent: Intent,
        @Named(Sermons) sermonsIntent: Intent
    ) = RoutingReducer(
        startupActivity,
        authenticationFragment,
        preflightChecklistFragment,
        googlePlayIntent,
        sermonsIntent
    )

    @Provides
    @Named(Sermons)
    fun provideSermonsIntent() = Intent(Intent.ACTION_VIEW, Uri.parse(StartupConstants.Intents.sermonsModule))
}
