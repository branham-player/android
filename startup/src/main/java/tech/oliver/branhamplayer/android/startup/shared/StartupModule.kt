package tech.oliver.branhamplayer.android.startup.shared

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import com.auth0.android.Auth0
import com.auth0.android.provider.WebAuthProvider
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.RouterTransaction
import org.koin.dsl.module.module
import tech.oliver.branhamplayer.android.startup.BuildConfig
import tech.oliver.branhamplayer.android.startup.StartupActivity
import tech.oliver.branhamplayer.android.startup.StartupActivityImpl
import tech.oliver.branhamplayer.android.startup.controllers.AuthenticationController
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.provider.CustomTabsOptions

val activityManagementModule = module {

    single(override = true) { (activity: StartupActivity) ->
        StartupActivityImpl(activity)
    }

    single(override = true) { (activity: StartupActivity, container: ViewGroup, savedInstanceState: Bundle) ->
        Conductor.attachRouter(activity, container, savedInstanceState)
    }

    single(override = true) {
        RouterTransaction.with(AuthenticationController())
    }
}

val auth0Module = module {

    factory(override = true) { (clientId: String, domain: String) ->
        Auth0(clientId, domain)
    }

    factory(override = true) {
        CustomTabsOptions.newBuilder()
    }

    factory(override = true) { (auth0: Auth0) ->
        WebAuthProvider.init(auth0)
    }

    factory(override = true) { (context: Context) ->
        val auth0 = Auth0(BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN)
        val apiClient = AuthenticationAPIClient(auth0)

        CredentialsManager(apiClient, SharedPreferencesStorage(context))
    }
}

val routingModule = module {

    factory(override = true) {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://oliver.tech/branham-player/sermons"))
    }
}

val startupModule = listOf(activityManagementModule, auth0Module, routingModule)
