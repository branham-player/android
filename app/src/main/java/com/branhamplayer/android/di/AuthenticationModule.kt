package com.branhamplayer.android.di

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.base.di.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {

    @FragmentScope
    @Provides
    fun getAuth0() = Auth0(BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN)

    @FragmentScope
    @Provides
    fun getAuthenticationAPIClient(auth0: Auth0) = AuthenticationAPIClient(auth0)

    @FragmentScope
    @Provides
    fun getCustomTabsOptionsBuilder(): CustomTabsOptions.Builder = CustomTabsOptions.newBuilder()

    @FragmentScope
    @Provides
    fun getWebAuthProviderBuilder(auth0: Auth0): WebAuthProvider.Builder = WebAuthProvider.init(auth0)

    @FragmentScope
    @Provides
    fun getCredentialsManager(authenticationAPIClient: AuthenticationAPIClient, context: Context) =
        CredentialsManager(authenticationAPIClient, SharedPreferencesStorage(context))
}
