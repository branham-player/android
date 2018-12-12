package tech.oliver.branhamplayer.android.startup.middleware

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext
import org.koin.standalone.get
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import tech.oliver.branhamplayer.android.startup.actions.AuthenticationAction
import tech.oliver.branhamplayer.android.startup.actions.RoutingAction
import tech.oliver.branhamplayer.android.startup.states.StartupState

class AuthenticationMiddleware {
    companion object {

        fun process(): Middleware<StartupState> = { dispatcher, _ ->
            { next ->
                { action ->
                    when (action) {
                        is AuthenticationAction.DoLoginAction -> doLogin(action.activity, dispatcher)
                        is AuthenticationAction.SaveCredentialsAction -> saveCredentials(action.context, action.credentials)
                    }

                    next(action)
                }
            }
        }

        private fun doLogin(activity: Activity, dispatch: DispatchFunction) {
            val auth0: WebAuthProvider.Builder = StandAloneContext.getKoin().koinContext.get()

            auth0.start(activity, object : AuthCallback {
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

        private fun saveCredentials(context: Context, credentials: Credentials) {
            val userCredentials: CredentialsManager = StandAloneContext.getKoin().koinContext.get { parametersOf(context) }
            userCredentials.saveCredentials(credentials)
        }
    }
}
