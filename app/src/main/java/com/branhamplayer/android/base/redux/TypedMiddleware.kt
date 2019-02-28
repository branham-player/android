package com.branhamplayer.android.base.redux

import org.rekotlin.Action
import org.rekotlin.DispatchFunction
import org.rekotlin.StateType

interface TypedMiddleware<ActionType : Action, MiddlewareStateType : StateType> {
    fun invoke(dispatch: DispatchFunction, action: ActionType, oldState: MiddlewareStateType?)
}
