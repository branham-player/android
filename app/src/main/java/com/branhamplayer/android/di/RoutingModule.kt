package com.branhamplayer.android.di

import android.content.Intent
import android.net.Uri
import com.branhamplayer.android.reducers.RoutingReducer
import com.branhamplayer.android.ui.AuthenticationFragment
import com.branhamplayer.android.ui.PreflightChecklistFragment
import com.branhamplayer.android.ui.StartupActivity
import dagger.Module
import dagger.Provides

@Module
class RoutingModule {

    @Provides
    fun provideRoutingReducer(
        startupActivity: StartupActivity,
        authenticationFragment: AuthenticationFragment,
        preflightChecklistFragment: PreflightChecklistFragment,
        sermonsIntent: Intent
    ) = RoutingReducer(startupActivity, authenticationFragment, preflightChecklistFragment, sermonsIntent)

    @Provides
    fun provideSermonsIntent() = Intent(Intent.ACTION_VIEW, Uri.parse("https://branhamplayer.com/sermons"))
}
