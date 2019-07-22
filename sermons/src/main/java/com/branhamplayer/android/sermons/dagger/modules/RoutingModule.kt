package com.branhamplayer.android.sermons.dagger.modules

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.branhamplayer.android.sermons.middleware.RoutingMiddleware
import com.branhamplayer.android.sermons.ui.SermonsActivity
import dagger.Module
import dagger.Provides

@Module
class RoutingModule {

    @Provides
    fun provideAppSettingsIntent(activity: SermonsActivity): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri

        return intent
    }

    @Provides
    fun provideRoutingMiddleware(
        activity: SermonsActivity,
        appSettingsIntent: Intent
    ) = RoutingMiddleware(
        activity = activity,
        appSettingsIntent = appSettingsIntent
    )
}
