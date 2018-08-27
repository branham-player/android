package tech.oliver.branhamplayer.android.files.repositories

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import tech.oliver.branhamplayer.android.base.Repository
import java.io.File

class FileListRepository : Repository {

    private val files: MutableLiveData<List<File>> = MutableLiveData()

    fun getFiles(): LiveData<List<File>> {
        val musicPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath
        val theTablePath = File(musicPath, "VGR")
        val fileList = theTablePath.listFiles()

        Logger.d("Determined audio path is: ${theTablePath.absolutePath}")

        if (fileList == null || fileList.isEmpty()) {
            Logger.i("There are no available files")

            files.value = emptyList()
            return files
        }

        Logger.d("Found ${fileList.size} audio files")

        files.value = fileList.toList()
        return files
    }
}
