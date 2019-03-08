package com.branhamplayer.android.actions

import org.rekotlin.Action

sealed class PreflightChecklistAction : Action {
    object CheckMessageAction : PreflightChecklistAction()
    object CheckMinimumVersionAction : PreflightChecklistAction()
    object CheckPlatformStatusAction : PreflightChecklistAction()

    data class NotifyWithMessageAction(val message: String) : PreflightChecklistAction()

    data class StopAppWithPlatformDownAction(val message: String) : PreflightChecklistAction()
    object StopAppWithMinimumVersionFailureAction : PreflightChecklistAction()
}
