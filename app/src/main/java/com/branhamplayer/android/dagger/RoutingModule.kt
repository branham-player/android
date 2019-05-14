package com.branhamplayer.android.dagger

import android.content.Intent
import android.net.Uri
import com.auth0.android.Auth0
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.branhamplayer.android.StartupConstants
import com.branhamplayer.android.reducers.RoutingReducer
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
    fun provideRoutingReducer(
        auth0: Auth0,
        customTabsOptionsBuilder: CustomTabsOptions.Builder,
        webAuthProvider: WebAuthProvider.Builder,
        @Named(GooglePlay) googlePlayIntent: Intent,
        @Named(Sermons) sermonsIntent: Intent
    ) = RoutingReducer(
        startupActivity,
        auth0,
        customTabsOptionsBuilder,
        webAuthProvider,
        googlePlayIntent,
        sermonsIntent
    )

    @Provides
    @Named(Sermons)
    fun provideSermonsIntent() = Intent(Intent.ACTION_VIEW, Uri.parse(StartupConstants.Intents.sermonsModule))
}
