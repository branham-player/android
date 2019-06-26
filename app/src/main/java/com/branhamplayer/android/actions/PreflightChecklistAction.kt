package com.branhamplayer.android.actions

import com.branhamplayer.android.base.redux.BaseAction

sealed class PreflightChecklistAction : BaseAction {
    object CheckMessageAction : PreflightChecklistAction()
    object CheckMetadataAction : PreflightChecklistAction()
    object CheckMinimumVersionAction : PreflightChecklistAction()
    object CheckPlatformStatusAction : PreflightChecklistAction()

    data class NotifyWithMessageAction(val message: String) : PreflightChecklistAction()

    object ResetMetadataUnavailableFlagAction : PreflightChecklistAction()
    object ResetPlatformUnavailableFlagAction : PreflightChecklistAction()

    object StopAppWithMetadataFailureAction : PreflightChecklistAction()
    object StopAppWithMinimumVersionFailureAction : PreflightChecklistAction()
    data class StopAppWithPlatformDownAction(val message: String) : PreflightChecklistAction()
}
