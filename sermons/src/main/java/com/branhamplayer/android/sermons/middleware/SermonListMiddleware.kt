package com.branhamplayer.android.sermons.middleware

import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.base.redux.TypedMiddleware
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.dagger.RxJavaModule
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.utils.permissions.PermissionConstants
import com.branhamplayer.android.sermons.utils.permissions.PermissionManager
import io.reactivex.Scheduler
import org.rekotlin.DispatchFunction
import javax.inject.Inject
import javax.inject.Named

class SermonListMiddleware @Inject constructor(
    private val activity: AppCompatActivity,
    @Named(RxJavaModule.BG) private val bg: Scheduler,
    @Named(RxJavaModule.UI) private val ui: Scheduler
) : TypedMiddleware<SermonListAction, SermonsState> {

    override fun invoke(dispatch: DispatchFunction, action: SermonListAction, oldState: SermonsState?) {
        when (action) {
            is SermonListAction.GetFileReadPermissionAction -> getFileReadPermission(dispatch)
            is SermonListAction.ShowPermissionDeniedErrorAction -> showPermissionDeniedError()
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
                    dispatcher(SermonListAction.FetchListAction)
                } else {
                    dispatcher(SermonListAction.ShowPermissionDeniedErrorAction)
                }
            }, {
                dispatcher(SermonListAction.ShowPermissionDeniedErrorAction)
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
