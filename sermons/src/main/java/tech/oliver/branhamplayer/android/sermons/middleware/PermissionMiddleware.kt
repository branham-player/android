package tech.oliver.branhamplayer.android.sermons.middleware

import android.app.Activity
import android.content.Context
import android.widget.Toast
import io.reactivex.Scheduler
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.actions.DataAction
import tech.oliver.branhamplayer.android.sermons.actions.PermissionAction
import tech.oliver.branhamplayer.android.sermons.shared.SermonsModuleConstants
import tech.oliver.branhamplayer.android.sermons.states.SermonsState
import tech.oliver.branhamplayer.android.sermons.utils.permissions.PermissionConstants
import tech.oliver.branhamplayer.android.sermons.utils.permissions.PermissionManager

class PermissionMiddleware {
    companion object {

        fun process(): Middleware<SermonsState> = { dispatcher, _ ->
            { next ->
                { action ->
                    when (action) {
                        is PermissionAction.GetFileReadPermissionAction -> getFileReadPermission(action.activity, dispatcher)
                        is PermissionAction.ShowPermissionDeniedErrorAction -> showPermissionDeniedError(action.context)
                    }

                    next(action)
                }
            }
        }

        private fun getFileReadPermission(activity: Activity, dispatcher: DispatchFunction) {
            val bg: Scheduler = StandAloneContext.getKoin().koinContext.get(SermonsModuleConstants.BG_THREAD)
            val ui: Scheduler = StandAloneContext.getKoin().koinContext.get(SermonsModuleConstants.UI_THREAD)

            // TODO: Use disposable
            PermissionManager(activity)
                    .getSinglePermission(PermissionConstants.fileRead)
                    .subscribeOn(bg)
                    .observeOn(ui)
                    .subscribe({ hasPermission ->
                        if (hasPermission) {
                            dispatcher(DataAction.FetchSermonListAction())
                        } else {
                            dispatcher(PermissionAction.ShowPermissionDeniedErrorAction(activity.applicationContext))
                        }
                    }, {
                        dispatcher(PermissionAction.ShowPermissionDeniedErrorAction(activity.applicationContext))
                    })
        }

        private fun showPermissionDeniedError(context: Context) {
            val toast: Toast = StandAloneContext.getKoin().koinContext.get {
                parametersOf(context, R.string.permission_denied_message, Toast.LENGTH_LONG)
            }

            toast.show()
        }
    }
}