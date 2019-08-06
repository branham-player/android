package com.branhamplayer.android.sermons.middleware

import android.annotation.SuppressLint
import com.branhamplayer.android.base.redux.TypedMiddleware
import com.branhamplayer.android.dagger.modules.RxJavaModule
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

    private fun checkFileReadPermission(dispatch: (Action) -> Unit) = permissionManager
        .checkPermissionStatus(PermissionConstants.fileRead)
        .subscribe({
            when (it) {
                PermissionManager.PermissionStatus.DeniedOnce -> dispatch(SermonListAction.ShowPermissionDeniedOnceErrorAction)
                PermissionManager.PermissionStatus.DeniedPermanently -> dispatch(SermonListAction.ShowPermissionDeniedPermanentlyErrorAction)

                PermissionManager.PermissionStatus.Granted -> {
                    dispatch(SermonListAction.ShowPermissionGrantedAction)
                    dispatch(SermonListAction.FetchListAction)
                }
            }
        }, {
            dispatch(SermonListAction.ShowPermissionDeniedOnceErrorAction)
        })
        .let {
            // TODO, dispose
        }

    @SuppressLint("CheckResult")
    private fun requestFileReadPermission(dispatcher: DispatchFunction) = permissionManager
        .requestPermission(PermissionConstants.fileRead)
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
