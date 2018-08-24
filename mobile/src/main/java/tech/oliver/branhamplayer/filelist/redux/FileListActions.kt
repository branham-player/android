package tech.oliver.branhamplayer.filelist.redux

import org.rekotlin.Action
import tech.oliver.branhamplayer.filelist.FileListViewModel

class PopulateFiles(val files: List<FileListViewModel>? = null) : Action
