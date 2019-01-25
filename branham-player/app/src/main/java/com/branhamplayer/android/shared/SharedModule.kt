package com.branhamplayer.android.shared

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.branhamplayer.android.BuildConfig
import org.koin.dsl.module.module

val auth0Module = module {

    factory(override = true) {
        Auth0(BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN)
    }

    factory(override = true) {
        AuthenticationAPIClient(get<Auth0>())
    }

    factory(override = true) { (context: Context) ->
        CredentialsManager(get(), SharedPreferencesStorage(context))
    }
}

val sharedModule = listOf(auth0Module)
