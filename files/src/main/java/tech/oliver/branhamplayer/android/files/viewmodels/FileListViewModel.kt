package tech.oliver.branhamplayer.android.files.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import tech.oliver.branhamplayer.android.files.mappers.FileListMapper
import tech.oliver.branhamplayer.android.files.models.FileModel
import tech.oliver.branhamplayer.android.files.repositories.FileListRepository

class FileListViewModel(
        private val repo: FileListRepository = FileListRepository(),
        private val mapper: FileListMapper = FileListMapper()
) : ViewModel() {

    val files: LiveData<List<FileModel>>
            get() = Transformations.switchMap(repo.getFiles()) {
                mapper.map(it)
            }
}
