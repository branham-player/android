package tech.oliver.branhamplayer.android.sermons.repositories

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import tech.oliver.branhamplayer.android.base.Repository
import java.io.File

class SermonsRepository : Repository {

    private val sermons: MutableLiveData<List<File>> = MutableLiveData()

    fun getSermons(): LiveData<List<File>> {
        val sermonsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath
        val theTableDirectory = File(sermonsDirectory, "VGR")
        val sermonList = theTableDirectory.listFiles()

        Logger.d("Determined sermon directory is: ${theTableDirectory.absolutePath}")

        if (sermonList == null || sermonList.isEmpty()) {
            Logger.i("There are no available sermons")

            sermons.value = emptyList()
            return sermons
        }

        Logger.d("Found ${sermonList.size} sermons")

        sermons.value = sermonList.toList()
        return sermons
    }
}
