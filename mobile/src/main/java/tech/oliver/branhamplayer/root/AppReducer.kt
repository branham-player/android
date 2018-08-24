package tech.oliver.branhamplayer.root

import org.rekotlin.Action
import tech.oliver.branhamplayer.filelist.redux.fileListReducer

fun appReducer(action: Action, state: AppState?): AppState =
        AppState(
                fileListState = fileListReducer(action, state?.fileListState)
        )
