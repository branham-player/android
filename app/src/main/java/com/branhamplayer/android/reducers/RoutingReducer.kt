package com.branhamplayer.android.reducers

import android.app.Dialog
import android.content.Intent
import androidx.navigation.findNavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.R
import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.di.RoutingModule
import com.branhamplayer.android.states.StartupState
import com.branhamplayer.android.store.startupStore
import com.branhamplayer.android.ui.StartupActivity
import javax.inject.Inject
import javax.inject.Named

class RoutingReducer @Inject constructor(
    private val startupActivity: StartupActivity,
    private val auth0: Auth0,
    private val customTabsOptionsBuilder: CustomTabsOptions.Builder,
    private val webAuthProvider: WebAuthProvider.Builder,
    @Named(RoutingModule.GooglePlay) private val googlePlayIntent: Intent,
    @Named(RoutingModule.Sermons) private val sermonsIntent: Intent
) : TypedReducer<RoutingAction, StartupState> {

    override fun invoke(action: RoutingAction, oldState: StartupState): StartupState {
        when (action) {
            is RoutingAction.CloseAppAction -> closeApp()
            is RoutingAction.NavigateToAuthenticationAction -> navigateToAuthentication()
            is RoutingAction.NavigateToGooglePlayStoreAction -> navigateToGooglePlayStore()
            is RoutingAction.NavigateToSermonsAction -> navigateToSermons()
            is RoutingAction.NavigateToWelcomeAction -> navigateToWelcome(oldState)
        }

        return oldState
    }

    private fun closeApp() = startupActivity.finish()

    private fun navigateToAuthentication() {
        auth0.isOIDCConformant = true

        val customTabsOptions = customTabsOptionsBuilder.withToolbarColor(R.color.toolbar_background).build()

        webAuthProvider
            .withCustomTabsOptions(customTabsOptions)
            .withScheme(BuildConfig.AUTH0_SCHEME)
            .withScope("openid profile email")
            .withAudience("https://${BuildConfig.AUTH0_DOMAIN}/userinfo")
            .start(startupActivity, object : AuthCallback {
                override fun onSuccess(credentials: Credentials) {
                    startupStore.dispatch(AuthenticationAction.SaveCredentialsAction(credentials))
                    startupStore.dispatch(RoutingAction.NavigateToSermonsAction)
                }

                override fun onFailure(dialog: Dialog) =
                    startupStore.dispatch(AuthenticationAction.ShowLoginErrorAction)

                override fun onFailure(exception: AuthenticationException?) =
                    startupStore.dispatch(AuthenticationAction.ShowLoginErrorAction)
            })
    }

    private fun navigateToGooglePlayStore() = startupActivity.startActivity(googlePlayIntent)

    private fun navigateToSermons() {
        sermonsIntent.addCategory(Intent.CATEGORY_BROWSABLE)
        sermonsIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        sermonsIntent.setPackage(startupActivity.packageName)

        startupActivity.startActivity(sermonsIntent)
    }

    private fun navigateToWelcome(oldState: StartupState): StartupState {
        startupActivity
            .findNavController(R.id.startup_navigation_host)
            .navigate(R.id.action_before_to_after_preflight_checklist_navigation_graph)

        return oldState.copy(
            ranPreflightChecklistSuccessfully = true
        )
    }
}
