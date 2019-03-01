package com.branhamplayer.android.sermons.middleware

import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.base.redux.TypedMiddleware
import com.branhamplayer.android.sermons.actions.DataAction
import com.branhamplayer.android.sermons.actions.PermissionAction
import com.branhamplayer.android.sermons.di.RxJavaModule
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.utils.permissions.PermissionConstants
import com.branhamplayer.android.sermons.utils.permissions.PermissionManager
import io.reactivex.Scheduler
import org.rekotlin.DispatchFunction
import javax.inject.Named

class PermissionMiddleware(
    private val activity: AppCompatActivity,
    @Named(RxJavaModule.BG) private val bg: Scheduler,
    @Named(RxJavaModule.UI) private val ui: Scheduler
) : TypedMiddleware<PermissionAction, SermonsState> {

    override fun invoke(dispatch: DispatchFunction, action: PermissionAction, oldState: SermonsState?) {
        when (action) {
            is PermissionAction.GetFileReadPermissionAction -> getFileReadPermission(dispatch)
            is PermissionAction.ShowPermissionDeniedErrorAction -> showPermissionDeniedError()
        }
    }

    private fun getFileReadPermission(dispatcher: DispatchFunction) {
        // TODO: Use disposable
        PermissionManager(activity)
            .getSinglePermission(PermissionConstants.fileRead)
            .subscribeOn(bg)
            .observeOn(ui)
            .subscribe({ hasPermission ->
                if (hasPermission) {
                    dispatcher(DataAction.FetchSermonListAction)
                } else {
                    dispatcher(PermissionAction.ShowPermissionDeniedErrorAction)
                }
            }, {
                dispatcher(PermissionAction.ShowPermissionDeniedErrorAction)
            })
    }

    private fun showPermissionDeniedError() {
        // TODO: Get rid of this whenever the design for the permission request is finished
        // TODO: No point in trying to work with this
//        val toast: Toast = StandAloneContext.getKoin().koinContext.get {
//            parametersOf(context, R.string.permission_denied_message, Toast.LENGTH_LONG)
//        }
//
//        toast.show()
    }
}
