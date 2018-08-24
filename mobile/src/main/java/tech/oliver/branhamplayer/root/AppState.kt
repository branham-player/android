package tech.oliver.branhamplayer.root

import org.rekotlin.StateType
import tech.oliver.branhamplayer.filelist.redux.FileListState

data class AppState(
        val fileListState: FileListState? = null
) : StateType
