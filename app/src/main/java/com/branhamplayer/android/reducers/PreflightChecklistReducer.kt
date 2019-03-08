package com.branhamplayer.android.reducers

import com.branhamplayer.android.actions.PreflightChecklistAction
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.states.StartupState

class PreflightChecklistReducer : TypedReducer<PreflightChecklistAction, StartupState> {

    override fun invoke(action: PreflightChecklistAction, oldState: StartupState) = when (action) {
        is PreflightChecklistAction.NotifyWithMessageAction -> notifyWithMessage(action.message, oldState)
        is PreflightChecklistAction.StopAppWithMinimumVersionFailureAction -> stopAppWithMinimumVersionFailure(oldState)
        is PreflightChecklistAction.StopAppWithPlatformDownAction -> stopWithPlatformDownAction(action.message, oldState)
        else -> oldState
    }

    private fun notifyWithMessage(message: String, oldState: StartupState) = oldState.copy(
        message = message
    )

    private fun stopAppWithMinimumVersionFailure(oldState: StartupState) = oldState.copy(
        minimumVersionMet = false
    )

    private fun stopWithPlatformDownAction(message: String, oldState: StartupState) = oldState.copy(
        message = message,
        platformAvailable = false
    )
}
