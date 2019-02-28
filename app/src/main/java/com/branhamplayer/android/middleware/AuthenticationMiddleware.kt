package com.branhamplayer.android.middleware

import android.app.Activity
import android.app.Dialog
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
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.states.StartupState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import javax.inject.Inject

class AuthenticationMiddleware @Inject constructor(
    private val activity: Activity,
    private val auth0: Auth0,
    private val customTabsOptionsBuilder: CustomTabsOptions.Builder,
    private val webAuthProvider: WebAuthProvider.Builder
) : Middleware<StartupState> {

    override fun invoke(
        dispatch: DispatchFunction,
        getState: () -> StartupState?
    ): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            when (action) {
                is AuthenticationAction.DoLoginAction -> doLogin(dispatch)
            }

            next(action)
        }
    }

    private fun doLogin(dispatch: DispatchFunction) {
        inject()

        auth0.isOIDCConformant = true

        val customTabsOptions = customTabsOptionsBuilder.withToolbarColor(R.color.toolbar_background).build()

        webAuthProvider
            .withCustomTabsOptions(customTabsOptions)
            .withScheme(BuildConfig.AUTH0_SCHEME)
            .withScope("openid profile email")
            .withAudience("https://${BuildConfig.AUTH0_DOMAIN}/userinfo")
            .start(activity, object : AuthCallback {
                override fun onSuccess(credentials: Credentials) {
                    dispatch(AuthenticationAction.SaveCredentialsAction(credentials))
                    dispatch(RoutingAction.NavigateToSermonsAction)
                }

                override fun onFailure(dialog: Dialog) =
                    dispatch(RoutingAction.ShowLoginErrorAction)

                override fun onFailure(exception: AuthenticationException?) =
                    dispatch(RoutingAction.ShowLoginErrorAction)
            })
    }

    private fun inject() {
        DaggerInjector.authenticationComponent?.inject(this)
    }
}
