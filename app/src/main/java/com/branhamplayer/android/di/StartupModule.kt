package com.branhamplayer.android.di

import android.app.Activity
import com.auth0.android.Auth0
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.branhamplayer.android.middleware.AuthenticationMiddleware
import dagger.Module
import dagger.Provides

@Module
class StartupModule {

    @Provides
    fun getAuthenticationMiddleware(
        activity: Activity,
        auth0: Auth0,
        customTabsOptionsBuilder: CustomTabsOptions.Builder,
        webAuthProvider: WebAuthProvider.Builder
    ) = AuthenticationMiddleware(
        activity, auth0, customTabsOptionsBuilder, webAuthProvider
    )
}
