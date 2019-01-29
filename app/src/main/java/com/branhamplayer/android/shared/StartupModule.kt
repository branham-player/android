package com.branhamplayer.android.shared

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.RouterTransaction
import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.MainActivity
import com.branhamplayer.android.MainActivityImpl
import com.branhamplayer.android.controllers.AuthenticationController
import org.koin.dsl.module.module

val activityManagementModule = module {

    single(override = true) { (activity: MainActivity) ->
        MainActivityImpl(activity)
    }

    single(override = true) { (activity: MainActivity, container: ViewGroup, savedInstanceState: Bundle) ->
        Conductor.attachRouter(activity, container, savedInstanceState)
    }

    single(override = true) {
        RouterTransaction.with(AuthenticationController())
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
