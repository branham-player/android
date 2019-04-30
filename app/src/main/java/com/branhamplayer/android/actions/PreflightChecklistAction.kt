package com.branhamplayer.android.actions

import com.branhamplayer.android.base.redux.BaseAction

sealed class PreflightChecklistAction : BaseAction {
    object CheckMessageAction : PreflightChecklistAction()
    object CheckMinimumVersionAction : PreflightChecklistAction()
    object CheckPlatformStatusAction : PreflightChecklistAction()

    data class NotifyWithMessageAction(val message: String) : PreflightChecklistAction()

    data class StopAppWithPlatformDownAction(val message: String) : PreflightChecklistAction()
    object StopAppWithMinimumVersionFailureAction : PreflightChecklistAction()
}
