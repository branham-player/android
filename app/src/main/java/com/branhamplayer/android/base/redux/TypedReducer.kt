package com.branhamplayer.android.base.redux

import org.rekotlin.Action
import org.rekotlin.StateType

interface TypedReducer<ActionType : Action, ReducerStateType : StateType> {
    fun invoke(action: ActionType, oldState: ReducerStateType): ReducerStateType
}
