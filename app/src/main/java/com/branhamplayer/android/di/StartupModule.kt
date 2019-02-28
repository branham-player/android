package com.branhamplayer.android.di

import android.app.Activity
import com.auth0.android.Auth0
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.middleware.AuthenticationMiddleware
import com.branhamplayer.android.ui.AuthenticationFragment
import dagger.Module
import dagger.Provides

@Module
class StartupModule(private val activity: Activity) {

    @Provides
    fun getActivity() = activity

    @Provides
    fun getAuth0() = Auth0(BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN)

    @Provides
    fun getAuthenticationMiddleware(
        activity: Activity,
        auth0: Auth0,
        customTabsOptionsBuilder: CustomTabsOptions.Builder,
        webAuthProvider: WebAuthProvider.Builder
    ) = AuthenticationMiddleware(
        activity, auth0, customTabsOptionsBuilder, webAuthProvider
    )

    @Provides
    fun getAuthenticationFragment() = AuthenticationFragment()

    @Provides
    fun getCustomTabsOptionsBuilder(): CustomTabsOptions.Builder = CustomTabsOptions.newBuilder()

    @Provides
    fun getWebAuthProviderBuilder(auth0: Auth0): WebAuthProvider.Builder = WebAuthProvider.init(auth0)
}
