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
import com.branhamplayer.android.states.StartupState
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware

class AuthenticationMiddleware {
    companion object {

        fun process(): Middleware<StartupState> = { dispatcher, _ ->
            { next ->
                { action ->
                    when (action) {
                        is AuthenticationAction.DoLoginAction -> doLogin(action.activity, dispatcher)
                    }

                    next(action)
                }
            }
        }

        private fun doLogin(activity: Activity, dispatch: DispatchFunction) {
            val auth0: Auth0 = StandAloneContext.getKoin().koinContext.get()
            auth0.isOIDCConformant = true

            val customTabsOptionsBuilder: CustomTabsOptions.Builder = StandAloneContext.getKoin().koinContext.get()
            val customTabsOptions = customTabsOptionsBuilder.withToolbarColor(R.color.toolbar_background).build()

            val webAuthProvider: WebAuthProvider.Builder = StandAloneContext.getKoin().koinContext.get { parametersOf(auth0) }

            webAuthProvider
                    .withCustomTabsOptions(customTabsOptions)
                    .withScheme(BuildConfig.AUTH0_SCHEME)
                    .withScope("openid profile email")
                    .withAudience("https://${BuildConfig.AUTH0_DOMAIN}/userinfo")
                    .start(activity, object : AuthCallback {
                        override fun onSuccess(credentials: Credentials) {
                            dispatch(AuthenticationAction.SaveCredentialsAction(activity.applicationContext, credentials))
                            dispatch(RoutingAction.NavigateToSermonsAction(activity.applicationContext))
                        }

                        override fun onFailure(dialog: Dialog) =
                                dispatch(RoutingAction.ShowLoginErrorAction())

                        override fun onFailure(exception: AuthenticationException?) =
                                dispatch(RoutingAction.ShowLoginErrorAction())
                    })
        }
    }
}
