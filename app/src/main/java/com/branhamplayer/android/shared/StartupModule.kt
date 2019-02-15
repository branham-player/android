package com.branhamplayer.android.shared

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.ui.AuthenticationFragment
import org.koin.dsl.module.module

val activityManagementModule = module {

    factory(override = true) {
        AuthenticationFragment()
    }
}

val auth0Module = module {

    factory(override = true) {
        Auth0(BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN)
    }

    factory(override = true) {
        AuthenticationAPIClient(get<Auth0>())
    }

    factory(override = true) {
        CustomTabsOptions.newBuilder()
    }

    factory(override = true) { (auth0: Auth0) ->
        WebAuthProvider.init(auth0)
    }

    factory(override = true) { (context: Context) ->
        CredentialsManager(get(), SharedPreferencesStorage(context))
    }
}

val routingModule = module {

    factory(override = true) {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://branhamplayer.com/sermons"))
    }
}

val startupModule = listOf(activityManagementModule, auth0Module, routingModule)
