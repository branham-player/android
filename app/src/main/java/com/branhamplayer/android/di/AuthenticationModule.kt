package com.branhamplayer.android.di

import android.app.Activity
import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.branhamplayer.android.BuildConfig
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule(private val activity: Activity) {

    @Provides
    fun getActivity() = activity

    @Provides
    fun getContext(): Context = activity.applicationContext

    @Provides
    fun getAuth0() = Auth0(BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN)

    @Provides
    fun getAuthenticationAPIClient(auth0: Auth0) = AuthenticationAPIClient(auth0)

    @Provides
    fun getCustomTabsOptionsBuilder(): CustomTabsOptions.Builder = CustomTabsOptions.newBuilder()

    @Provides
    fun getWebAuthProviderBuilder(auth0: Auth0): WebAuthProvider.Builder = WebAuthProvider.init(auth0)

    @Provides
    fun getCredentialsManager(authenticationAPIClient: AuthenticationAPIClient, context: Context) =
        CredentialsManager(authenticationAPIClient, SharedPreferencesStorage(context))
}
