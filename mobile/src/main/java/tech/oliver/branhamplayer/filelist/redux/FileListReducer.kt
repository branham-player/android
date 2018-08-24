package tech.oliver.branhamplayer.filelist.redux

import android.os.Environment
import com.orhanobut.logger.Logger
import org.rekotlin.Action
import tech.oliver.branhamplayer.filelist.FileListMapper
import java.io.File

fun fileListReducer(action: Action, fileListState: FileListState?): FileListState {
    var state = fileListState ?: FileListState()

    when (action) {
        is PopulateFiles -> state = populateFiles(state)
    }

    return state
}

fun populateFiles(fileListState: FileListState): FileListState {
    val musicPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath
    val theTablePath = File(musicPath, "VGR")
    val files = theTablePath.listFiles()

    Logger.d("Determined audio path is: ${theTablePath.absolutePath}")

    if (files == null || files.isEmpty()) {
        Logger.i("There are no available files")
        return fileListState.copy(files = emptyList())
    }

    Logger.d("Found ${files.size} audio files")

    val mappedFiles = FileListMapper().map(files.toList())

    return fileListState.copy(files = mappedFiles)
}
