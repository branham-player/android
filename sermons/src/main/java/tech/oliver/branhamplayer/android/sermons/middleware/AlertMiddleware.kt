package tech.oliver.branhamplayer.android.sermons.middleware

import android.content.Context
import android.widget.Toast
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext
import org.rekotlin.Middleware
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.actions.AlertAction
import tech.oliver.branhamplayer.android.sermons.states.SermonsState

class AlertMiddleware {
    companion object {

        fun process(): Middleware<SermonsState> = { _, _ ->
            { next ->
                { action ->
                    when (action) {
                        is AlertAction.ShowPermissionDeniedErrorAction -> showPermissionDeniedError(action.context)
                    }

                    next(action)
                }
            }
        }

        private fun showPermissionDeniedError(context: Context) {
            val toast: Toast = StandAloneContext.getKoin().koinContext.get {
                parametersOf(context, R.string.permission_denied_message, Toast.LENGTH_LONG)
            }

            toast.show()
        }
    }
}
