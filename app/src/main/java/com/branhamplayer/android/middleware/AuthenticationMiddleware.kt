package com.branhamplayer.android.middleware

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
import com.branhamplayer.android.di.AuthenticationModule
import com.branhamplayer.android.di.DaggerAuthenticationComponent
import com.branhamplayer.android.states.StartupState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import javax.inject.Inject

class AuthenticationMiddleware : Middleware<StartupState> {

    @JvmField
    @Inject
    var auth0: Auth0? = null

    @JvmField
    @Inject
    var customTabsOptionsBuilder: CustomTabsOptions.Builder? = null

    @JvmField
    @Inject
    var webAuthProvider: WebAuthProvider.Builder? = null

    override fun invoke(
        dispatch: DispatchFunction,
        getState: () -> StartupState?
    ): (DispatchFunction) -> DispatchFunction = { next ->
        { action ->
            when (action) {
                is AuthenticationAction.DoLoginAction -> doLogin(action, dispatch)
            }

            next(action)
        }
    }

    private fun doLogin(action: AuthenticationAction.DoLoginAction, dispatch: DispatchFunction) {

        DaggerAuthenticationComponent
            .builder()
            .authenticationModule(AuthenticationModule(action.activity))
            .build()
            .inject(this)

        auth0?.isOIDCConformant = true

        val activity = action.activity
        val customTabsOptions = customTabsOptionsBuilder?.withToolbarColor(R.color.toolbar_background)?.build()

        customTabsOptions?.let {
            webAuthProvider
                ?.withCustomTabsOptions(it)
                ?.withScheme(BuildConfig.AUTH0_SCHEME)
                ?.withScope("openid profile email")
                ?.withAudience("https://${BuildConfig.AUTH0_DOMAIN}/userinfo")
                ?.start(activity, object : AuthCallback {
                    override fun onSuccess(credentials: Credentials) {
                        dispatch(AuthenticationAction.SaveCredentialsAction(activity.applicationContext, credentials))
                        dispatch(RoutingAction.NavigateToSermonsAction(activity.applicationContext))
                    }

                    override fun onFailure(dialog: Dialog) =
                        dispatch(RoutingAction.ShowLoginErrorAction)

                    override fun onFailure(exception: AuthenticationException?) =
                        dispatch(RoutingAction.ShowLoginErrorAction)
                })
        }
    }
}
