package tech.oliver.branhamplayer.filelist.redux

import org.rekotlin.StateType
import tech.oliver.branhamplayer.filelist.FileListViewModel

data class FileListState(
        val files: List<FileListViewModel>? = emptyList()
) : StateType
