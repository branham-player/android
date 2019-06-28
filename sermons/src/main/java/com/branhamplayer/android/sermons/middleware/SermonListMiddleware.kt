package com.branhamplayer.android.sermons.middleware

import android.annotation.SuppressLint
import com.branhamplayer.android.base.redux.TypedMiddleware
import com.branhamplayer.android.dagger.RxJavaModule
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.utils.permissions.PermissionConstants
import com.branhamplayer.android.sermons.utils.permissions.PermissionManager
import io.reactivex.Scheduler
import org.rekotlin.Action
import org.rekotlin.DispatchFunction
import javax.inject.Inject
import javax.inject.Named

class SermonListMiddleware @Inject constructor(
    private val permissionManager: PermissionManager,
    @Named(RxJavaModule.BG) private val bg: Scheduler,
    @Named(RxJavaModule.UI) private val ui: Scheduler
) : TypedMiddleware<SermonListAction, SermonsState> {

    override fun invoke(dispatch: DispatchFunction, action: SermonListAction, oldState: SermonsState?) {
        when (action) {
            is SermonListAction.CheckFileReadPermissionAction -> checkFileReadPermission(dispatch)
            is SermonListAction.RequestFileReadPermissionAction -> requestFileReadPermission(dispatch)
        }
    }

    private fun checkFileReadPermission(dispatch: (Action) -> Unit) =
        if (permissionManager.hasPermission(PermissionConstants.fileRead)) {
            dispatch(SermonListAction.FetchListAction)
        } else {
            dispatch(SermonListAction.ShowPermissionNotYetRequestedAction)
        }

    @SuppressLint("CheckResult")
    private fun requestFileReadPermission(dispatcher: DispatchFunction) = permissionManager
        .getSinglePermission(PermissionConstants.fileRead)
        .subscribeOn(bg)
        .observeOn(ui)
        .subscribe({ status ->
            when (status) {
                PermissionManager.PermissionStatus.DeniedOnce ->
                    dispatcher(SermonListAction.ShowPermissionDeniedOnceErrorAction)

                PermissionManager.PermissionStatus.DeniedPermanently ->
                    dispatcher(SermonListAction.ShowPermissionDeniedPermanentlyErrorAction)

                PermissionManager.PermissionStatus.Granted -> {
                    dispatcher(SermonListAction.ShowPermissionGrantedAction)
                    dispatcher(SermonListAction.FetchListAction)
                }
            }
        }, {
            dispatcher(SermonListAction.ShowPermissionDeniedOnceErrorAction)
        })
}
