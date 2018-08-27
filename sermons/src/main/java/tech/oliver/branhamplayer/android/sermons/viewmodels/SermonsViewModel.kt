package tech.oliver.branhamplayer.android.sermons.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import tech.oliver.branhamplayer.android.sermons.mappers.SermonMapper
import tech.oliver.branhamplayer.android.sermons.models.SermonModel
import tech.oliver.branhamplayer.android.sermons.repositories.SermonsRepository

class SermonsViewModel(
        private val repo: SermonsRepository = SermonsRepository(),
        private val mapper: SermonMapper = SermonMapper()
) : ViewModel() {

    val sermons: LiveData<List<SermonModel>>
            get() = Transformations.switchMap(repo.getSermons()) {
                mapper.map(it)
            }
}
