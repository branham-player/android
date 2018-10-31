package tech.oliver.branhamplayer.android.reducers

import org.rekotlin.Action
import tech.oliver.branhamplayer.android.states.AppState

fun appReducer(action: Action, appState: AppState?) = appState ?: AppState()
