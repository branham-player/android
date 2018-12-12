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
import tech.oliver.branhamplayer.android.startup.StartUpActivity
import tech.oliver.branhamplayer.android.startup.StartupActivityImpl
import tech.oliver.branhamplayer.android.startup.controllers.AuthenticationController
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.AuthenticationAPIClient

val startupModule = module {

    // region Auth0

    factory(override = true) {
        val auth0 = Auth0(BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN)
        auth0.isOIDCConformant = true

        WebAuthProvider.init(auth0)
                .withScheme(BuildConfig.AUTH0_SCHEME)
                .withAudience("https://${BuildConfig.AUTH0_DOMAIN}/userinfo")
    }

    factory(override = true) { (context: Context) ->
        val auth0 = Auth0(BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN)
        val apiClient = AuthenticationAPIClient(auth0)

        CredentialsManager(apiClient, SharedPreferencesStorage(context))
    }

    // endregion

    // region Routing

    factory {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://oliver.tech/branham-player/sermons"))
    }

    // endregion

    // region Start Up Activity Creation and Routing

    single { (activity: StartUpActivity) ->
        StartupActivityImpl(activity)
    }

    single { (activity: StartUpActivity, container: ViewGroup, savedInstanceState: Bundle) ->
        Conductor.attachRouter(activity, container, savedInstanceState)
    }

    single {
        RouterTransaction.with(AuthenticationController())
    }

    // endregion
}
