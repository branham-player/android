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
import com.branhamplayer.android.middleware.AuthenticationMiddleware
import com.branhamplayer.android.reducers.AuthenticationReducer
import com.branhamplayer.android.services.auth0.Auth0Service
import com.branhamplayer.android.ui.AuthenticationFragment
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {

    @Provides
    fun provideAuth0() = Auth0(BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN)

    @Provides
    fun provideAuth0Service(authClient: AuthenticationAPIClient, credentialsManager: CredentialsManager) =
        Auth0Service(authClient, credentialsManager)

    @Provides
    fun provideAuthenticationAPIClient(auth0: Auth0) = AuthenticationAPIClient(auth0)

    @Provides
    fun provideAuthenticationFragment() = AuthenticationFragment()

    @Provides
    fun provideAuthenticationMiddleware(
        activity: Activity,
        auth0: Auth0,
        customTabsOptionsBuilder: CustomTabsOptions.Builder,
        webAuthProvider: WebAuthProvider.Builder
    ) = AuthenticationMiddleware(
        activity, auth0, customTabsOptionsBuilder, webAuthProvider
    )

    @Provides
    fun provideAuthenticationReducer(userCredentials: CredentialsManager) =
        AuthenticationReducer(userCredentials)

    @Provides
    fun provideCredentialsManager(authenticationAPIClient: AuthenticationAPIClient, context: Context) =
        CredentialsManager(authenticationAPIClient, SharedPreferencesStorage(context))

    @Provides
    fun provideCustomTabsOptionsBuilder(): CustomTabsOptions.Builder = CustomTabsOptions.newBuilder()

    @Provides
    fun provideWebAuthProviderBuilder(auth0: Auth0): WebAuthProvider.Builder = WebAuthProvider.init(auth0)
}
